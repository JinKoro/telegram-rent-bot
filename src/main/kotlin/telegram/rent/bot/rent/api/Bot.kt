package telegram.rent.bot.rent.api

import com.google.inject.Inject
import kotlinx.coroutines.launch
import telegram.rent.bot.infrastructure.bot.Builder
import telegram.rent.bot.infrastructure.bot.CommandHandler
import telegram.rent.bot.infrastructure.bot.Rent
import telegram.rent.bot.rent.infrastructure.extensions.commands.sendApartment
import telegram.rent.bot.rent.infrastructure.extensions.commands.sendMessage

class Bot @Inject constructor(
    @Rent private val builder: Builder,
): CommandHandler(builder) {

    init {
        command("start") {
            launch {
                sendApartment(
                    channels = builder.channels
                )
            }
        }

        command("hello") {
            sendMessage(
                text = "Hello! I'm bot.",
                channels = builder.channels
            )
        }
    }
}
