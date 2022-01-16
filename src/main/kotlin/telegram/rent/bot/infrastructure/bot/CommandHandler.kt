package telegram.rent.bot.infrastructure.bot

import com.github.kotlintelegrambot.dispatcher.handlers.CommandHandlerEnvironment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

open class CommandHandler(
    private val builder: Builder
): CoroutineScope by CoroutineScope(Dispatchers.IO) {
    fun command(name: String, body: CommandHandlerEnvironment.() -> Unit) {
        builder.commands.add(Command(name, body))
    }
}
