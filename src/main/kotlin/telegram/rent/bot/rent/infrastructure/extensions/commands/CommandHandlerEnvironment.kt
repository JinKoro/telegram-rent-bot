package telegram.rent.bot.rent.infrastructure.extensions.commands

import com.github.kotlintelegrambot.dispatcher.handlers.CommandHandlerEnvironment
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.ParseMode
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import org.slf4j.LoggerFactory
import telegram.rent.bot.infrastructure.configuration.ChannelName
import telegram.rent.bot.rent.parsing.parser.KufarParser
import telegram.rent.bot.rent.parsing.parser.OnlinerParser
import telegram.rent.bot.rent.parsing.parser.RealtParser

private const val POST_DELAY = 3000L
private const val RESTART_DELAY = 10000L

fun CommandHandlerEnvironment.sendMessage(
    text: String,
    channels: List<ChannelName>,
    parseMode: ParseMode = ParseMode.MARKDOWN
) {
    channels.forEach { name ->
        bot.sendMessage(
            chatId = ChatId.fromChannelUsername(name),
            text = text,
            parseMode = parseMode
        )
    }
}

suspend fun CommandHandlerEnvironment.sendApartment(
    channels: List<ChannelName>,
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
                        sendMessage(apartment.toString(), channels, parseMode)
                        delay(POST_DELAY)
                    }
                delay(RESTART_DELAY)
            } catch (logging: Exception) {
                logger.debug("Apartment parsing exception:" + logging.message)
            }
        }
    }
}
