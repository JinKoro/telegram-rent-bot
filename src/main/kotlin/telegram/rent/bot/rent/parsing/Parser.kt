package telegram.rent.bot.rent.parsing

import telegram.rent.bot.rent.infrastructure.Apartment

interface Parser {
    suspend fun parse(): List<Apartment>
}
