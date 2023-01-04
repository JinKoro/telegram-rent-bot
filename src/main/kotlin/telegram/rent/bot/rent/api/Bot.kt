package telegram.rent.bot.rent.api

import com.google.inject.Inject
import telegram.rent.bot.infrastructure.bot.Builder
import telegram.rent.bot.infrastructure.bot.CommandHandler
import telegram.rent.bot.infrastructure.bot.Rent
import telegram.rent.bot.rent.infrastructure.extensions.commands.sendApartment
import telegram.rent.bot.rent.infrastructure.extensions.commands.sendMessage

class Bot @Inject constructor(
    @Rent private val builder: Builder,
): CommandHandler(builder) {

    init {

        command("live") {
            sendMessage(text = "Live")
        }

        command("start") {
            sendMessage(text = "Start parsing")
            sendApartment(
                channels = builder.channels
            )
        }

        command("hello") {
            sendMessage(
                text = "Hello! I'm bot.",
                channels = builder.channels
            )
        }

        command("sanya") {
            sendMessage(
                text = "Саня педик! \uD83D\uDC33",
                channels = builder.channels
            )
        }

        command("dimon") {
            sendMessage(
                text = """
                    Призывнику Дминтрию Скребцу!
                    Вам надлежит к 9 часам 27.09.2022 
                    прибыть в военный коммисариат вашего города.
                    
                    Военный коммисар Коротин Д.C
                """.trimIndent(),
                channels = builder.channels
            )
        }
    }
}
