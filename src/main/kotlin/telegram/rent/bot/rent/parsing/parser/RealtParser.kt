package telegram.rent.bot.rent.parsing.parser

import io.ktor.client.call.body
import io.ktor.client.request.get
import java.time.LocalDateTime
import java.time.ZoneId
import org.json.XML
import telegram.rent.bot.rent.infrastructure.Apartment
import telegram.rent.bot.rent.infrastructure.Link
import telegram.rent.bot.rent.parsing.HttpClient
import telegram.rent.bot.rent.parsing.Parser
import telegram.rent.bot.rent.parsing.data.Realt

class RealtParser : Parser, HttpClient() {
    private var lastUpdated: LocalDateTime = LocalDateTime.now(ZoneId.of("Europe/Minsk"))

    override suspend fun parse(): List<Apartment> {
        val newApartments = mutableListOf<Apartment>()
        val response: String = client.get(Link.REALT_MINSK).body()
        val jsonString = XML.toJSONObject(response).toString()
        val document = json.decodeFromString(Realt.serializer(), jsonString)

        document.apartments
            .forEach { realtApartment ->
                val apartment = realtApartment.transform()
                if (apartment.announcement.updatedAt > lastUpdated) {
                    newApartments.add(apartment)
                }
            }
            .also {
                if (newApartments.isNotEmpty()) {
                    lastUpdated = newApartments.maxOf { it.announcement.updatedAt }
                }
            }
        return newApartments
    }
}
