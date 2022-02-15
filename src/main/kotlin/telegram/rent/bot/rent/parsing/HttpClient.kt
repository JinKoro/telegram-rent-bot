package telegram.rent.bot.rent.parsing

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.http.ContentType
import kotlinx.serialization.json.Json

open class HttpClient {
    protected val client: HttpClient
        get() = HttpClient(CIO) {
            install(JsonFeature) {
                accept(ContentType.Application.Json)
                serializer = KotlinxSerializer(json)
            }
    }

    protected val json: Json
        get() = Json {
            isLenient = true
            ignoreUnknownKeys = true
        }
}
