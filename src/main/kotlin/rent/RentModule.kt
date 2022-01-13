package rent

import com.google.inject.AbstractModule
import rent.api.Bot

class RentModule : AbstractModule() {
    override fun configure() {
        bind(Bot::class.java).asEagerSingleton()
    }
}
