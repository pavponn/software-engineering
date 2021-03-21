package turnstile.command

import common.command.Command
import common.command.CommandsHandler
import common.command.UnknownCommandException
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import turnstile.dao.TurnstileCommandsDao
import java.lang.Exception
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

import java.util.Locale

import java.time.format.FormatStyle


class TurnstileCommandsHandler(private val dao: TurnstileCommandsDao) : CommandsHandler {

    val formatter: DateTimeFormatter = DateTimeFormatter
        .ofLocalizedDateTime(FormatStyle.SHORT)
        .withLocale(Locale.UK)
        .withZone(ZoneId.systemDefault())

    override suspend fun handle(command: Command): String = when (command) {
        is EnterCommand -> {
            dao.enter(command.userId, command.time)
            "User(id=${command.userId}) entered"
        }
        is ExitCommand -> {
            val (startTime, eventId) = dao.exit(command.userId, command.time)

            val params = mapOf<String, String>(
                "userId" to "${command.userId}",
                "startTime" to formatter.format(startTime),
                "endTime" to formatter.format(command.time),
                "eventId" to "$eventId"
            )
            GlobalScope.launch {
                val response = try {
                    sendVisitRequest(params)
                } catch (e: Exception) {
                    "ERROR: ${e.message}"
                }
                println("Response for exit request: $response")
            }
            "User(id=${command.userId}) exited"
        }
        else -> throw UnknownCommandException(command)
    }

    private suspend fun sendVisitRequest(params: Map<String, String>): String {
        val url = "http://localhost:2020/report/visit?" +
                params.map { "${it.key}=${it.value}" }
                    .joinToString("&")
        println("Request url: $url")
        return HttpClient().post(url)
    }

}