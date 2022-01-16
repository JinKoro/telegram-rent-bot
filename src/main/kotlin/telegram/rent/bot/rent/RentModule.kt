package telegram.rent.bot.rent

import com.google.inject.AbstractModule
import telegram.rent.bot.rent.api.Bot

class RentModule : AbstractModule() {
    override fun configure() {
        bind(Bot::class.java).asEagerSingleton()
    }
}
