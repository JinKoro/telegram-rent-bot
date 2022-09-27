package telegram.rent.bot.infrastructure.bot

import com.github.kotlintelegrambot.dispatcher.handlers.CommandHandlerEnvironment

open class CommandHandler(
    private val builder: Builder
) {
    fun command(name: String, body: CommandHandlerEnvironment.() -> Unit) {
        builder.commands.add(Command(name, body))
    }
}
