package telegram.rent.bot.rent.infrastructure.extensions.commands

import com.github.kotlintelegrambot.dispatcher.handlers.CommandHandlerEnvironment
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.ParseMode
import kotlinx.coroutines.delay
import telegram.rent.bot.infrastructure.configuration.ChannelName
import telegram.rent.bot.rent.parsing.Worker

private const val POST_DELAY = 3000L

fun CommandHandlerEnvironment.sendMessage(
    text: String,
    channels: List<ChannelName> = emptyList(),
    parseMode: ParseMode = ParseMode.MARKDOWN
) {
    if (channels.isEmpty()) {
        bot.sendMessage(
            chatId = ChatId.fromId(message.chat.id),
            text = text,
            parseMode = parseMode
        )
    }
    channels.forEach { name ->
        bot.sendMessage(
            chatId = ChatId.fromChannelUsername(name),
            text = text,
            parseMode = parseMode
        )
    }
}

fun CommandHandlerEnvironment.sendApartment(
    channels: List<ChannelName>,
    parseMode: ParseMode = ParseMode.MARKDOWN
) {
    Worker.start(
        { apartment ->
            sendMessage(apartment.toString(), channels, parseMode)
            delay(POST_DELAY)
        },
        { exception ->
            sendMessage(exception, emptyList(), parseMode)
        }
    )
}
