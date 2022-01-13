package rent.api

import com.google.inject.Inject
import infrastructure.bot.Builder
import infrastructure.bot.CommandHandler
import infrastructure.bot.Rent
import kotlinx.coroutines.launch
import rent.infrastructure.extensions.commands.sendApartment

class Bot @Inject constructor(
    @Rent private val builder: Builder,
): CommandHandler(builder) {

    init {
        command("start") {
            launch {
                sendApartment(builder.botName)
            }
        }
    }
}
