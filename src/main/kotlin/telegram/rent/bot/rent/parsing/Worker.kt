package telegram.rent.bot.rent.parsing

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import telegram.rent.bot.rent.infrastructure.Apartment
import telegram.rent.bot.rent.parsing.parser.KufarParser
import telegram.rent.bot.rent.parsing.parser.OnlinerParser
import telegram.rent.bot.rent.parsing.parser.RealtParser

object Worker: CoroutineScope by CoroutineScope(Dispatchers.IO + SupervisorJob()) {
    private const val RESTART_DELAY = 60000L
    private val parsers = listOf<Parser>(
        KufarParser(),
        RealtParser(),
        OnlinerParser()
    )

    fun start(body: suspend (Apartment) -> Unit, exception: (String) -> Unit) = launch {
        while (isActive) {
            val deferred = mutableListOf<Deferred<List<Apartment>>>()
            parsers
                .map { parser ->
                    async {
                        try { parser.parse() } catch (logging: Exception) {
                            exception("Apartment parsing error with parser (${parser::class.simpleName}): "
                                + logging.message)
                            emptyList()
                        }
                    }.apply { deferred.add(this) }.await()
                }
                .flatten()
                .filter {
                    it.type == Apartment.Type.TWO_ROOMS && it.price < Apartment.Price.maxPrice
                }
                .sortedBy { it.announcement.updatedAt }
                .forEach { body(it) }

            deferred.forEach { it.cancelChildren(); it.cancel() }
            delay(RESTART_DELAY)
        }
    }
}
