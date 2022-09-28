package telegram.rent.bot.rent.parsing

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import telegram.rent.bot.rent.infrastructure.Apartment
import telegram.rent.bot.rent.parsing.parser.KufarParser
import telegram.rent.bot.rent.parsing.parser.OnlinerParser
import telegram.rent.bot.rent.parsing.parser.RealtParser

object Worker: CoroutineScope by CoroutineScope(Dispatchers.IO + SupervisorJob()) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private const val RESTART_DELAY = 60000L
    private val parsers = listOf<Parser>(
        KufarParser(),
        RealtParser(),
        OnlinerParser()
    )

    fun start(body: suspend (Apartment) -> Unit, exception: (String) -> Unit) = launch {
        logger.info("Start parsing...")
        while (isActive) {
            parsers
                .map { parser ->
                    try { parser.parse() } catch (ignore: Exception) {
                        exception(
                            "Apartment parsing error with parser (${parser::class.simpleName}): " + ignore.message
                        ); emptyList()
                    }
                }
                .flatten()
                .filter { it.type == Apartment.Type.TWO_ROOMS && it.price < Apartment.Price.maxPrice }
                .sortedBy { it.announcement.updatedAt }
                .forEach { body(it) }

            delay(RESTART_DELAY)
        }
    }
}
