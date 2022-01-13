package infrastructure.configuration

import com.sksamuel.hoplite.ConfigLoader
import com.sksamuel.hoplite.EnvironmentVariablesPropertySource
import com.sksamuel.hoplite.PropertySource

object Configuration {
    fun load(): Config {
        return ConfigLoader.Builder()
            .addSource(PropertySource.resource("/local.properties", optional = true))
            .addSource(PropertySource.resource("/default.properties", optional = true))
            .addSource(
                EnvironmentVariablesPropertySource(
                    useUnderscoresAsSeparator = true,
                    allowUppercaseNames = true
                )
            )
            .build()
            .loadConfigOrThrow<Network>().network
    }
}

data class Network(val network: Config = Config())
data class Config(val telegram: Telegram = Telegram())
data class Telegram(val bot: Bot = Bot())
data class Bot(val name: String = "<channel_name>", val token: String = "<token>")
