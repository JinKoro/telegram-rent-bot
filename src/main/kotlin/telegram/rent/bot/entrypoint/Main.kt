package telegram.rent.bot.entrypoint

import com.google.inject.AbstractModule
import com.google.inject.Guice
import telegram.rent.bot.infrastructure.bot.Builder
import telegram.rent.bot.infrastructure.bot.Dispatcher
import telegram.rent.bot.infrastructure.bot.Rent
import telegram.rent.bot.infrastructure.configuration.Config
import telegram.rent.bot.infrastructure.configuration.Configuration
import telegram.rent.bot.rent.RentModule

fun main() {
    val configuration = Configuration.load()
    val dispatcher = Dispatcher()
    val rentBuilder = Builder(
        configuration.rent.channels,
        configuration.rent.bot.token,
        dispatcher
    )
    val injector = Guice.createInjector(object : AbstractModule() {
        override fun configure() {
            bind(Config::class.java).toInstance(configuration)
            bind(Builder::class.java).annotatedWith(Rent::class.java).toInstance(rentBuilder)
            bind(Dispatcher::class.java).toInstance(dispatcher)
        }
    })
    injector.createChildInjector(RentModule())

    val rentBot = rentBuilder.build()
    rentBot.startPolling()
}
