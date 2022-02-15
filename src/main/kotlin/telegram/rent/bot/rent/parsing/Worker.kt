package telegram.rent.bot.rent.parsing

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import telegram.rent.bot.rent.infrastructure.Apartment
import telegram.rent.bot.rent.parsing.parser.KufarParser
import telegram.rent.bot.rent.parsing.parser.OnlinerParser
import telegram.rent.bot.rent.parsing.parser.RealtParser

object Worker : CoroutineScope by CoroutineScope(Dispatchers.IO) {
    private const val RESTART_DELAY = 10000L
    private val apartments = MutableSharedFlow<Apartment>(200)
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val parsers = listOf<Parser>(
        KufarParser(),
        RealtParser(),
        OnlinerParser()
    )

    suspend fun start(body: suspend (Apartment) -> Unit) = coroutineScope {
        launch {
            apartments.collect {
                body(it)
            }
        }
        launch {
            while (isActive) {
                emit()
                delay(RESTART_DELAY)
            }
        }
    }

    private suspend fun emit() = coroutineScope {
        try {
            parsers
                .map { parser -> async { parser.parse() } }.awaitAll()
                .flatten()
                .sortedBy { it.announcement.updatedAt }
                .forEach { apartments.emit(it) }
        } catch (logging: Exception) {
            logger.debug("Apartment parsing exception:" + logging.message)
        }
    }
}
