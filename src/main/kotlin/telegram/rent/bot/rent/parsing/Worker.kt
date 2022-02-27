package telegram.rent.bot.rent.parsing

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import org.slf4j.LoggerFactory
import telegram.rent.bot.rent.infrastructure.Apartment
import telegram.rent.bot.rent.parsing.parser.KufarParser
import telegram.rent.bot.rent.parsing.parser.OnlinerParser
import telegram.rent.bot.rent.parsing.parser.RealtParser

object Worker {
    private const val RESTART_DELAY = 10000L
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val parsers = listOf<Parser>(
        KufarParser(),
        RealtParser(),
        OnlinerParser()
    )

    suspend fun start(body: suspend (Apartment) -> Unit) =
        try {
            while (true) {
                emit(body)
                delay(RESTART_DELAY)
            }
        } catch (logging: Exception) {
            logger.debug("Apartment parsing exception:" + logging.message)
        }

    private suspend fun emit(body: suspend (Apartment) -> Unit) = coroutineScope {
        parsers
            .map { parser -> async { parser.parse() } }.awaitAll()
            .flatten()
            .sortedBy { it.announcement.updatedAt }
            .forEach { body(it) }
    }
}