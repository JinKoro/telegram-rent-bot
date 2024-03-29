package telegram.rent.bot.infrastructure.configuration

import com.sksamuel.hoplite.ConfigLoader
import com.sksamuel.hoplite.PropertySource

object Configuration {
    fun load(): Config {
        return ConfigLoader.builder()
            .addSource(PropertySource.resource("/local.properties", optional = true))
            .addSource(PropertySource.environment(useUnderscoresAsSeparator = true, allowUppercaseNames = true))
            .build()
            .loadConfigOrThrow<Telegram>().telegram
    }
}

data class Telegram(val telegram: Config = Config())
data class Config(val rent: Rent = Rent())
data class Rent(val bot: Bot = Bot(), val channels: List<ChannelName> = emptyList())
data class Bot(val token: String = "<token>")

typealias ChannelName = String
