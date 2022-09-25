package telegram.rent.bot.rent.parsing

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

open class HttpClient {
    protected val client: HttpClient
        get() = HttpClient(CIO) {
            install(ContentNegotiation) {
                json(json)
            }
        }

    protected val json: Json
        get() = Json {
            isLenient = true
            ignoreUnknownKeys = true
        }
}
