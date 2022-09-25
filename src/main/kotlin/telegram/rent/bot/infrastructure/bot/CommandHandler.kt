package telegram.rent.bot.infrastructure.bot

import com.github.kotlintelegrambot.dispatcher.handlers.CommandHandlerEnvironment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

open class CommandHandler(
    private val builder: Builder
): CoroutineScope by CoroutineScope(Dispatchers.IO + SupervisorJob()) {
    fun command(name: String, body: CommandHandlerEnvironment.() -> Unit) {
        builder.commands.add(Command(name, body))
    }
}
