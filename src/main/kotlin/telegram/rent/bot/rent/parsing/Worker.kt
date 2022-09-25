package telegram.rent.bot.rent.parsing

import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import telegram.rent.bot.rent.infrastructure.Apartment
import telegram.rent.bot.rent.parsing.parser.KufarParser
import telegram.rent.bot.rent.parsing.parser.OnlinerParser
import telegram.rent.bot.rent.parsing.parser.RealtParser

object Worker {
    private const val RESTART_DELAY = 60000L
    private val parsers = listOf<Parser>(
        KufarParser(),
        RealtParser(),
        OnlinerParser()
    )

    suspend fun start(
        body: suspend (Apartment) -> Unit,
        exception: (String) -> Unit
    ): Job = coroutineScope {
        launch {
            while (isActive) {
                parsers
                    .map { parser ->
                        async {
                            try { parser.parse() } catch (logging: Exception) {
                                exception("Apartment parsing error with parser (${parser::class.simpleName}): "
                                    + logging.message)
                                emptyList()
                            }
                        }
                    }.awaitAll()
                    .flatten()
                    .filter {
                        it.type == Apartment.Type.TWO_ROOMS && it.price < Apartment.Price.maxPrice
                    }
                    .sortedBy { it.announcement.updatedAt }
                    .forEach { body(it) }

                delay(RESTART_DELAY)
            }
        }
    }
}
