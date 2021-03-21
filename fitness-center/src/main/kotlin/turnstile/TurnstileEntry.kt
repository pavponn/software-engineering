package turnstile

import common.clock.Clock
import common.clock.NormalClock
import common.connection.PostgresConnection
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.runBlocking
import turnstile.command.EnterCommand
import turnstile.command.ExitCommand
import turnstile.command.TurnstileCommandsHandler
import turnstile.dao.DatabaseTurnstileCommandsDao

fun main(): Unit = runBlocking {
    val clock: Clock = NormalClock()
    val connection = PostgresConnection.getConnection()
    val commandsDao = DatabaseTurnstileCommandsDao(connection)
    val commandsHandler = TurnstileCommandsHandler(commandsDao)
    embeddedServer(Netty, port = 2121) {
        routing {
            post("/turnstile/enter") {
                val userId = call.request.queryParameters["userId"]?.toLong()
                if (userId == null) {
                    call.badRequest()
                } else {
                    try {
                        val command = EnterCommand(userId, clock.now())
                        val result = commandsHandler.handle(command)
                        call.respondText(result)
                    } catch (e: Exception) {
                        call.error(e.message)
                    }

                }
            }
            post("/turnstile/exit") {
                val userId = call.request.queryParameters["userId"]?.toLong()
                if (userId == null) {
                    call.badRequest()
                } else {
                    try {
                        val command = ExitCommand(userId, clock.now())
                        val result = commandsHandler.handle(command)
                        call.respondText(result)
                    } catch (e: Exception) {
                        call.error(e.message)
                    }

                }
            }
        }
    }.start(wait = true)
    Unit

}

suspend fun ApplicationCall.badRequest(): Unit = respondText("Incorrect parameters", status = HttpStatusCode.BadRequest)

suspend fun ApplicationCall.error(errorMsg: String?): Unit =
    respondText("Error occurred: $errorMsg", status = HttpStatusCode.BadRequest)