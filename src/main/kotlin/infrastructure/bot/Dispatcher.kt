package infrastructure.bot

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

open class Dispatcher : CoroutineScope by CoroutineScope(Dispatchers.IO) {
    fun dispatch(builder: Bot.Builder, commands: List<Command>) {
        builder.dispatch {
            commands.forEach { command ->
                command(command.name, command.body)
            }
        }
    }
}
