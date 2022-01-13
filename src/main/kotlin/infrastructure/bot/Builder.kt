package infrastructure.bot

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.logging.LogLevel

data class Builder(
    val botName: String,
    val token: String,
    val dispatcher: Dispatcher,
    val logLevel: LogLevel = LogLevel.All()
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
