package telegram.rent.bot.rent.parsing.parser

import io.ktor.client.call.body
import io.ktor.client.request.get
import java.time.LocalDateTime
import java.time.ZoneId
import telegram.rent.bot.rent.infrastructure.Apartment
import telegram.rent.bot.rent.infrastructure.Link
import telegram.rent.bot.rent.parsing.HttpClient
import telegram.rent.bot.rent.parsing.Parser
import telegram.rent.bot.rent.parsing.data.Kufar

class KufarParser : Parser, HttpClient() {
    private var lastUpdated: LocalDateTime = LocalDateTime.now(ZoneId.of("Europe/Minsk"))

    override suspend fun parse(): List<Apartment> {
        val newApartments = mutableListOf<Apartment>()
        val document: Kufar = client.get(Link.KUFAR_MINSK).body()

        document.apartments
            .forEach { kufarApartment ->
                val apartment = kufarApartment.transform()
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
