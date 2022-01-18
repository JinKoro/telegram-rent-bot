package telegram.rent.bot.rent.parsing.parser

import io.ktor.client.request.get
import java.time.LocalDateTime
import org.json.XML
import org.slf4j.LoggerFactory
import telegram.rent.bot.rent.infrastructure.Apartment
import telegram.rent.bot.rent.infrastructure.Link
import telegram.rent.bot.rent.parsing.HttpClient
import telegram.rent.bot.rent.parsing.Parser
import telegram.rent.bot.rent.parsing.data.Realt

class RealtParser : Parser, HttpClient() {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private var lastUpdated: LocalDateTime = LocalDateTime.MIN

    override suspend fun parse(): List<Apartment> {
        val newApartments = mutableListOf<Apartment>()
        val response = client.get<String>(Link.REALT_MINSK)
        val jsonString = XML.toJSONObject(response).toString()
        val document = json.decodeFromString(Realt.serializer(), jsonString)

        document.apartments
            .asReversed()
            .forEach { realtApartment ->
                try {
                    val apartment = realtApartment.transform()
                    if (apartment.announcement.updatedAt <= lastUpdated) {
                        return emptyList()
                    }
                    newApartments.add(apartment)
                } catch (logging: Exception) {
                    logger.debug(logging.message)
                }
            }
            .also { lastUpdated = newApartments.last().announcement.updatedAt }
        return newApartments
    }
}