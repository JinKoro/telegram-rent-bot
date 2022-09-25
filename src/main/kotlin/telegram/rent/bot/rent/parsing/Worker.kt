package telegram.rent.bot.rent.parsing

import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import telegram.rent.bot.rent.infrastructure.Apartment
import telegram.rent.bot.rent.parsing.parser.KufarParser
import telegram.rent.bot.rent.parsing.parser.OnlinerParser
import telegram.rent.bot.rent.parsing.parser.RealtParser

object Worker {
    private const val RESTART_DELAY = 60000L
    private val logger = LoggerFactory.getLogger(javaClass)
    private val parsers = listOf<Parser>(
        KufarParser(),
        RealtParser(),
        OnlinerParser()
    )

    suspend fun start(body: suspend (Apartment) -> Unit): Job = coroutineScope {
        launch {
            while (isActive) {
                parsers
                    .map { parser ->
                        async {
                            try { parser.parse() } catch (logging: Exception) {
                                logger.error("Apartment parsing error with parser (${parser::class.simpleName}): " + logging.message)
                                emptyList()
                            }
                        }
                    }.awaitAll()
                    .flatten()
                    .sortedBy { it.announcement.updatedAt }
                    .forEach { body(it) }

                delay(RESTART_DELAY)
            }
        }
    }
}
