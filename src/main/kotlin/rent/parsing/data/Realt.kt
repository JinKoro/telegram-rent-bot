package rent.parsing.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import rent.infrastructure.Apartment
import rent.infrastructure.Link
import rent.infrastructure.MapLink
import rent.infrastructure.Site
import rent.infrastructure.extensions.datetime.toLocalDateTimeRealt

@Serializable
data class Realt(val data: Data) {

    @Serializable
    data class Data(val objects: RealtObjects) {

        @Serializable
        data class RealtObjects(
            @SerialName("object")
            val apartments: List<RealtApartment>
        )

        @Serializable
        data class RealtApartment(
            private val code: String,
            private val address: String,
            private val description: String,
            private val district: String,
            @SerialName("area_total")
            private val area: Double,
            @SerialName("type")
            private val typeString: String,
            @SerialName("thumb")
            private val photoLink: String? = null,
            @SerialName("lat")
            private val latitude: Double,
            @SerialName("lon")
            private val longitude: Double,
            @SerialName("price")
            private val priceString: String,
            @SerialName("date_revision")
            private val updatedAt: String
        ) {
            fun transform() = Apartment(
                photoLink = photoLink ?: Link.NO_PHOTO_LINK,
                location = location,
                type = type,
                announcement = announcement,
                price = price
            )

            @Transient
            private val type = when {
                typeString.contains("1-комнатная") -> Apartment.Type.ONE_ROOMS
                typeString.contains("2-комнатная") -> Apartment.Type.TWO_ROOMS
                typeString.contains("3-комнатная") -> Apartment.Type.THREE_ROOMS
                typeString.contains("4-комнатная") -> Apartment.Type.FOUR_ROOMS
                typeString.contains("5-комнатная") -> Apartment.Type.FIVE_ROOMS
                typeString.contains("6-комнатная") -> Apartment.Type.SIX_ROOMS
                typeString.contains("студия") -> Apartment.Type.STUDIO
                typeString.lowercase().contains("комната")-> Apartment.Type.ROOM
                else -> throw UnsupportedOperationException("Unknown room type: $typeString")
            }

            @Transient
            private val announcement = Apartment.Announcement(
                url = "https://realt.by/rent/flat-for-long/object/$code",
                updatedAt = updatedAt.toLocalDateTimeRealt(),
                site = Site.REALT,
                info = description
            )

            @Transient
            private val price = priceString.let { price ->
                val newPrice = price.replace("\\u00a0".toRegex(), "")
                val (amt, cur) = Regex("([0-9| ]{1,6})([$]|€|руб)").find(newPrice)!!.destructured
                val amount = amt.toDouble()
                val currency = Apartment.Price.Currency.values().single { it.symbols.contains(cur) }

                Apartment.Price(amount, currency)
            }

            @Transient
            private val coordinate = Apartment.Location.Coordinate(latitude, longitude)

            @Transient
            private val location = Apartment.Location(
                address,
                address,
                coordinate,
                MapLink.GOOGLE.invoke(coordinate)
            )
        }
    }

    @Transient
    val apartments = data.objects.apartments
}
