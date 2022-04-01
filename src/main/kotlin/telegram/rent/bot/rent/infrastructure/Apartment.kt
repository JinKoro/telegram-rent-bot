package telegram.rent.bot.rent.infrastructure

import java.time.LocalDateTime
import kotlinx.serialization.Serializable
import telegram.rent.bot.infrastructure.date.LocalDateTimeSerializer
import telegram.rent.bot.rent.infrastructure.extensions.datetime.toTimeFormat

@Serializable
data class Apartment(
    val photoLink: String,
    val location: Location,
    val price: Price,
    val type: Type,
    val announcement: Announcement
) {
    override fun toString(): String {
        return """
        🐳 *${type.typeName} за $price 💸*
        
        🏠 Место: *${location.address}*        
        ⌚ Время: *${announcement.updatedAt.toTimeFormat()}*        
  
        ${if(announcement.info != null) { "⌚ Описание: ${announcement.info}" } else ""}
        🌧🌧🌧🌧🌧🌧🌧🌧🌧🌧🌧🌧🌧
        
        📸 [Фото]($photoLink)
        
        🗺 [Карта](${location.mapLink})
        
        🔍 [Источник](${announcement.url})     _${announcement.site.name.lowercase().replaceFirstChar { it.uppercase() }}_
        
        """.trimIndent()
    }

    @Serializable
    data class Location(
        val city: String,
        val address: String,
        val coordinate: Coordinate,
        val mapLink: String
    ) {
        @Serializable
        data class Coordinate(
            val latitude: Double,
            val longitude: Double
        )
    }

    @Serializable
    data class Price(
        val amount: Double,
        val currency: Currency
    ) {
        override fun toString(): String {
            if (amount == 0.0) return "Цена не указана"
            return "${amount.toInt()}${currency.symbols.first()}"
        }
        enum class Currency(val symbols: List<String>) {
            USD(listOf("$")),
            BYN(listOf("Br", "руб")),
            EUR(listOf("€")),
            RUB(listOf("₽"))
        }
    }

    enum class Type(val typeName: String) {
        ROOM("Комната"),
        STUDIO("Студия"),
        ONE_ROOMS("1-Комнатная"),
        TWO_ROOMS("2-Комнатная"),
        THREE_ROOMS("3-Комнатная"),
        FOUR_ROOMS("4-Комнатная"),
        FIVE_ROOMS("5-Комнатная"),
        SIX_ROOMS("6-Комнатная")
    }

    @Serializable
    data class Announcement(
        val url: String,
        @Serializable(LocalDateTimeSerializer::class)
        val updatedAt: LocalDateTime,
        val site: Site,
        val info: String? = null
    )
}
