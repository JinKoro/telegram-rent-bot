package rent.parsing

import rent.infrastructure.Apartment

interface Parser {
    suspend fun parse(): List<Apartment>
}
