package turnstile.command

import common.command.Command
import common.command.CommandsHandler
import common.command.UnknownCommandException
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import turnstile.dao.TurnstileCommandsDao


class TurnstileCommandsHandler(private val dao: TurnstileCommandsDao) : CommandsHandler {
    override suspend fun handle(command: Command): String = when (command) {
        is EnterCommand -> {
            dao.enter(command.memberId, command.time)
            "Member(id=${command.memberId}) entered"
        }
        is ExitCommand -> {
            dao.exit(command.memberId, command.time)
            GlobalScope.launch {
                val response = try {
                    sendVisitRequest(command.memberId)
                } catch (e: Exception) {
                    "Error: ${e.message}"
                }
                println("Response for update request: $response")
            }
            "Member(id=${command.memberId}) exited"
        }
        else -> throw UnknownCommandException(command)
    }

    private suspend fun sendVisitRequest(memberId: Long): String {
        val url = "http://localhost:2020/report/update/$memberId"
        println("Request url: $url")
        return HttpClient().post(url)
    }

}