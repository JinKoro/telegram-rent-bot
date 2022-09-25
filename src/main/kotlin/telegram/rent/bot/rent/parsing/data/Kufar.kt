package telegram.rent.bot.rent.parsing.data

import java.time.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.double
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import telegram.rent.bot.rent.infrastructure.Apartment
import telegram.rent.bot.rent.infrastructure.Link
import telegram.rent.bot.rent.infrastructure.MapLink
import telegram.rent.bot.rent.infrastructure.Site

@Serializable
data class Kufar(
    @SerialName("ads")
    val apartments: List<KufarApartment>
) {
    @Serializable
    data class KufarApartment(
        @SerialName("account_parameters")
        private val announcementParameters: List<AnnouncementParameter>,
        @SerialName("ad_link")
        private val url: String,
        @SerialName("ad_parameters")
        private val apartmentParameters: List<ApartmentParameter>,
        @SerialName("currency")
        private val currencyString: String,
        @SerialName("list_time")
        private val updatedAt: String,
        @SerialName("images")
        private val photos: List<Photo>? = null,
        @SerialName("price_usd")
        private val amount: Double
    ) {
        fun transform() = Apartment(
            type = type,
            photoLink = photoLink,
            announcement = announcement,
            location = location,
            price = price,
        )

        @Transient
        private val photoLink = photos?.firstOrNull()?.photoLink() ?: Link.NO_PHOTO_LINK

        @Transient
        private val coordinate = apartmentParameters
            .first { params -> params.name == "Координаты" }.value.jsonArray
            .let { coordinate ->
                Apartment.Location.Coordinate(
                    longitude = coordinate[0].jsonPrimitive.double,
                    latitude = coordinate[1].jsonPrimitive.double
                )
            }

        @Transient
        private val type = apartmentParameters
            .first { params -> params.name == "Количество комнат" || params.name == "Комнат" }.value.jsonPrimitive.int
            .let { rooms ->
                when (rooms) {
                    0 -> Apartment.Type.ROOM
                    1 -> Apartment.Type.ONE_ROOMS
                    2 -> Apartment.Type.TWO_ROOMS
                    3 -> Apartment.Type.THREE_ROOMS
                    4 -> Apartment.Type.FOUR_ROOMS
                    5 -> Apartment.Type.FIVE_ROOMS
                    6 -> Apartment.Type.SIX_ROOMS
                    else -> throw UnsupportedOperationException("Unknown room type: $rooms")
                }
            }

        @Transient
        private val announcement = Apartment.Announcement(
            url = url,
            updatedAt = LocalDateTime.parse(updatedAt.dropLast(1)).plusHours(3),
            site = Site.KUFAR
        )

        @Transient
        private val location = Apartment.Location(
            city = announcementParameters.last().value,
            address = announcementParameters.last().value,
            coordinate = coordinate,
            mapLink = MapLink.GOOGLE(coordinate)
        )

        @Transient
        private val price = Apartment.Price(
            amount = amount / 100,
            currency = when (currencyString) {
                "USD" -> Apartment.Price.Currency.USD
                "BYN" -> Apartment.Price.Currency.BYN
                "BYR" -> Apartment.Price.Currency.BYN
                "EUR" -> Apartment.Price.Currency.EUR
                "RUB" -> Apartment.Price.Currency.RUB
                else -> throw UnsupportedOperationException("Unknown currency symbol: $currencyString")
            }
        )

        @Serializable
        data class Photo(val id: String) {
            fun photoLink(): String {
                return Link.KUFAR_PHOTO +
                    "${id.substring(0..1)}/" +
                    "$id.jpg?rule=gallery"
            }
        }

        @Serializable
        data class AnnouncementParameter(
            @SerialName("v")
            val value: String
        )

        @Serializable
        data class ApartmentParameter(
            @SerialName("pl")
            val name: String,
            @SerialName("v")
            val value: JsonElement
        )
    }
}
