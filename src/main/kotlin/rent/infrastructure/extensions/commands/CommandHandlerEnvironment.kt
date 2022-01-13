package rent.infrastructure.extensions.commands

import com.github.kotlintelegrambot.dispatcher.handlers.CommandHandlerEnvironment
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.ParseMode
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import org.slf4j.LoggerFactory
import rent.parsing.parser.KufarParser
import rent.parsing.parser.OnlinerParser
import rent.parsing.parser.RealtParser

private const val POST_DELAY = 3000L
private const val RESTART_DELAY = 10000L

suspend fun CommandHandlerEnvironment.sendMessage(
    botName: String,
    text: String,
    parseMode: ParseMode = ParseMode.MARKDOWN
) {
    bot.sendMessage(
        chatId = ChatId.fromChannelUsername(botName),
        text = text,
        parseMode = parseMode
    )
}

suspend fun CommandHandlerEnvironment.sendApartment(
    botName: String,
    parseMode: ParseMode = ParseMode.MARKDOWN
) {
    val logger = LoggerFactory.getLogger(javaClass)
    val parsers = listOf(
        KufarParser(),
        RealtParser(),
        OnlinerParser(),
    )

    coroutineScope {
        while (isActive) {
            try {
                parsers
                    .map { parser -> async { parser.parse() } }.awaitAll()
                    .flatten()
                    .sortedBy { it.announcement.updatedAt }
                    .forEach { apartment ->
                        sendMessage(botName, apartment.toString(), parseMode)
                        delay(POST_DELAY)
                    }
                delay(RESTART_DELAY)
            } catch (logging: Exception) {
                logger.debug("Apartment parsing exception:" + logging.message)
            }
        }
    }
}
