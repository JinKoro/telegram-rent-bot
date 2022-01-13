package entrypoint

import com.google.inject.AbstractModule
import com.google.inject.Guice
import infrastructure.bot.Builder
import infrastructure.bot.Dispatcher
import infrastructure.bot.Rent
import infrastructure.configuration.Config
import infrastructure.configuration.Configuration
import rent.RentModule

fun main() {
    val configuration = Configuration.load()
    val dispatcher = Dispatcher()
    val rentBuilder = Builder(
        configuration.telegram.bot.name,
        configuration.telegram.bot.token,
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
