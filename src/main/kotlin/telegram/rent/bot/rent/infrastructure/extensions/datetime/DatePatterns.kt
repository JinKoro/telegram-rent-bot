package telegram.rent.bot.rent.infrastructure.extensions.datetime

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DatePatterns {
    const val DATE_TIME = "dd MMMM | HH:mm"
    const val REALT_DATE_TIME = "dd.MM.yyyy HH:mm"
}

fun LocalDateTime.toTimeFormat(): String {
    return format(
        DateTimeFormatter.ofPattern(DatePatterns.DATE_TIME)
    )
}

fun String.toLocalDateTimeRealt(): LocalDateTime {
    return LocalDateTime.from(DateTimeFormatter.ofPattern(DatePatterns.REALT_DATE_TIME).parse(this))
}
