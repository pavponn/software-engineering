package turnstile

import base.clock.Clock
import base.clock.NormalClock
import base.connection.PostgresConnection
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.runBlocking
import turnstile.command.EnterCommand
import turnstile.command.ExitCommand
import turnstile.command.TurnstileCommandsRouter
import turnstile.dao.DatabaseTurnstileCommandsDao

fun main(): Unit = runBlocking {
    val clock: Clock = NormalClock()
    val connection = PostgresConnection.getConnection()
    val commandsDao = DatabaseTurnstileCommandsDao(connection)
    val commandsHandler = TurnstileCommandsRouter(commandsDao)
    embeddedServer(Netty, port = 2121) {
        routing {
            post("/turnstile/enter") {
                val memberId = call.request.queryParameters["memberId"]?.toLong()
                if (memberId == null) {
                    call.badRequest()
                } else {
                    try {
                        val command = EnterCommand(memberId, clock.now())
                        val result = commandsHandler.route(command)
                        call.respondText(result)
                    } catch (e: Exception) {
                        call.error(e.message)
                    }

                }
            }
            post("/turnstile/exit") {
                val memberId = call.request.queryParameters["memberId"]?.toLong()
                if (memberId == null) {
                    call.badRequest()
                } else {
                    try {
                        val command = ExitCommand(memberId, clock.now())
                        val result = commandsHandler.route(command)
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