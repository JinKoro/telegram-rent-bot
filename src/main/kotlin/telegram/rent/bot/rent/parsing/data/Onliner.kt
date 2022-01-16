package telegram.rent.bot.rent.parsing.data

import java.time.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonElement
import telegram.rent.bot.rent.infrastructure.Apartment
import telegram.rent.bot.rent.infrastructure.MapLink
import telegram.rent.bot.rent.infrastructure.Site

@Serializable
data class Onliner(
    val apartments: List<OnlinerApartment>
) {
    @Serializable
    data class OnlinerApartment(
        private val id: String,
        @SerialName("price")
        private val priceString: Price,
        @SerialName("rent_type")
        private val typeString: String,
        @SerialName("location")
        private val locationString: Location,
        @SerialName("photo")
        private val photoLink: String,
        @SerialName("last_time_up")
        private val lastTimeUp: String,
        private val url: String
    ) {

        fun transform() = Apartment(
            announcement = announcement,
            photoLink = photoLink,
            location = location,
            price = price,
            type = type
        )

        @Transient
        private val announcement = Apartment.Announcement(
            url = url,
            updatedAt = LocalDateTime.parse(lastTimeUp.dropLast(5)),
            site = Site.ONLINER
        )

        @Transient
        private val location = Apartment.Location(
            city = locationString.address,
            address = locationString.address,
            coordinate = locationString.coordinate,
            mapLink = MapLink.GOOGLE.invoke(locationString.coordinate)
        )

        @Transient
        private val price = Apartment.Price(
            amount = priceString.amount,
            currency = priceString.currency
        )

        @Transient
        private val type =
            when (typeString) {
                "room" -> Apartment.Type.ROOM
                "studio" -> Apartment.Type.STUDIO
                "1_room" -> Apartment.Type.ONE_ROOMS
                "2_rooms" -> Apartment.Type.TWO_ROOMS
                "3_rooms" -> Apartment.Type.THREE_ROOMS
                "4_rooms" -> Apartment.Type.FOUR_ROOMS
                "5_rooms" -> Apartment.Type.FIVE_ROOMS
                "6_rooms" -> Apartment.Type.SIX_ROOMS
                else -> throw UnsupportedOperationException("Unknown room type: $typeString")
            }

        @Serializable
        data class Price(
            val amount: Double,
            @SerialName("currency")
            val currencyString: String,
            val converted: JsonElement
        ) {
            @Transient
            val currency = when (currencyString) {
                "USD" -> Apartment.Price.Currency.USD
                "BYN" -> Apartment.Price.Currency.BYN
                "EUR" -> Apartment.Price.Currency.EUR
                "RUB" -> Apartment.Price.Currency.RUB
                else -> throw UnsupportedOperationException("Unknown currency symbol: $currencyString")
            }
        }

        @Serializable
        data class Location(
            val address: String,
            @SerialName("user_address")
            val userAddress: String,
            val latitude: Double,
            val longitude: Double
        ) {
            @Transient
            val coordinate = Apartment.Location.Coordinate(
                latitude,
                longitude
            )
        }
    }
}
