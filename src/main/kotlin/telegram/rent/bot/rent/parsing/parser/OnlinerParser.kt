package telegram.rent.bot.rent.parsing.parser

import io.ktor.client.request.get
import java.time.LocalDateTime
import org.slf4j.LoggerFactory
import telegram.rent.bot.rent.infrastructure.Apartment
import telegram.rent.bot.rent.infrastructure.Link
import telegram.rent.bot.rent.parsing.HttpClient
import telegram.rent.bot.rent.parsing.Parser
import telegram.rent.bot.rent.parsing.data.Onliner

class OnlinerParser : Parser, HttpClient() {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private var lastUpdated: LocalDateTime = LocalDateTime.now()

    override suspend fun parse(): List<Apartment> {
        val newApartments = mutableListOf<Apartment>()
        val document = client.get<Onliner>(Link.ONLINER_MINSK)

        document.apartments
            .forEach { onlinerApartment ->
                try {
                    val apartment = onlinerApartment.transform()
                    if (apartment.announcement.updatedAt > lastUpdated) {
                        newApartments.add(apartment)
                    }
                } catch (logging: Exception) {
                    logger.debug(logging.message)
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
