package rent.parsing.parser

import io.ktor.client.request.get
import java.time.LocalDateTime
import org.slf4j.LoggerFactory
import rent.infrastructure.Apartment
import rent.infrastructure.Link
import rent.parsing.HttpClient
import rent.parsing.Parser
import rent.parsing.data.Kufar

class KufarParser : Parser, HttpClient() {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private var lastUpdated: LocalDateTime = LocalDateTime.MIN

    override suspend fun parse(): List<Apartment> {
        val newApartments = mutableListOf<Apartment>()
        val document = client.get<Kufar>(Link.KUFAR_MINSK)

        document.apartments
            .asReversed()
            .forEach { kufarApartment ->
                try {
                    val apartment = kufarApartment.transform()
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
