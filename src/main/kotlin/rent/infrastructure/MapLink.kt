package rent.infrastructure

enum class MapLink {
    GOOGLE {
        override fun invoke(
            coordinate: Apartment.Location.Coordinate
        ): String {
            return Link.GOOGLE_MAPS +
                "${coordinate.latitude}," +
                "${coordinate.longitude}"
        }
    };
    abstract operator fun invoke(coordinate: Apartment.Location.Coordinate): String
}
