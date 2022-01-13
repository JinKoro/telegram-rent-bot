package infrastructure.bot

import com.github.kotlintelegrambot.dispatcher.handlers.CommandHandlerEnvironment

data class Command(
    val name: String,
    val body: CommandHandlerEnvironment.() -> Unit
)
