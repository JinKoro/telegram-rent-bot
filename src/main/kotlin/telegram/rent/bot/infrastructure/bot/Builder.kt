package telegram.rent.bot.infrastructure.bot

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.logging.LogLevel
import telegram.rent.bot.infrastructure.configuration.ChannelName

data class Builder(
    val channels: List<ChannelName>,
    val token: String,
    val dispatcher: Dispatcher,
    val logLevel: LogLevel = LogLevel.Error
) {
    val commands: MutableList<Command> = mutableListOf()

    fun build(): Bot {
        return Bot.Builder().build {
            token = this@Builder.token
            logLevel = this@Builder.logLevel
            dispatcher.dispatch(this, commands)
        }
    }
}
