package turnstile.command

import base.command.Command
import base.command.CommandsRouter
import base.command.UnknownCommandException
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import turnstile.dao.TurnstileCommandsDao


class TurnstileCommandsRouter(private val dao: TurnstileCommandsDao) : CommandsRouter {
    override suspend fun route(command: Command): String = when (command) {
        is EnterCommand -> {
            dao.enter(command.memberId, command.time)
            "Member(id=${command.memberId}) entered"
        }
        is ExitCommand -> {
            dao.exit(command.memberId, command.time)
            updateRequest(command.memberId)
            "Member(id=${command.memberId}) exited"
        }
        else -> throw UnknownCommandException(command)
    }

    private suspend fun sendVisitRequest(memberId: Long): String {
        val url = "http://localhost:2020/report/update/$memberId"
        println("Request url: $url")
        return HttpClient().post(url)
    }

    private suspend fun updateRequest(memberId: Long) {
        GlobalScope.launch {
            val response = try {
                sendVisitRequest(memberId)
            } catch (e: Exception) {
                "Error: ${e.message}"
            }
            println("Response for update request: $response")
        }
    }


}