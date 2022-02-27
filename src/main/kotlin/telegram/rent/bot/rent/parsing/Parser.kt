package telegram.rent.bot.rent.parsing

import telegram.rent.bot.rent.infrastructure.Apartment

fun interface Parser {
    suspend fun parse(): List<Apartment>
}
