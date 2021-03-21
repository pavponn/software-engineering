package admin

import admin.command.AddUserCommand
import admin.command.AdminCommandsHandler
import admin.command.RenewSubscriptionCommand
import admin.dao.DatabaseAdminCommandsDao
import admin.dao.DatabaseAdminQueriesDao
import admin.query.AdminQueriesHandler
import admin.query.GetUserInfoQuery
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
import java.time.Instant


fun main() = runBlocking {
    val clock: Clock = NormalClock()
    val connection = PostgresConnection.getConnection()
    val commandsDao = DatabaseAdminCommandsDao(connection)
    val queriesDao = DatabaseAdminQueriesDao(connection)
    val commandsHandler = AdminCommandsHandler(commandsDao)
    val queriesHandler = AdminQueriesHandler(queriesDao)
    embeddedServer(Netty, port = 2122) {
        routing {
            post("/admin/user") {
                val name = call.request.queryParameters["name"]
                if (name == null) {
                    call.badRequest()
                } else {
                    val command = AddUserCommand(name)
                    val result = commandsHandler.handle(command)
                    call.respondText(result)
                }
            }
            post("/admin/subscription") {
                val userId = call.request.queryParameters["userId"]?.toLong()
                val endTimeString = call.request.queryParameters["endTime"]
                if (userId == null || endTimeString == null) {
                    call.badRequest()
                } else {
                    try {
                        val endTime = Instant.parse(endTimeString)
                        val command = RenewSubscriptionCommand(userId, endTime)
                        val result = commandsHandler.handle(command)
                        call.respondText(result)
                    } catch (e: Exception) {
                        call.error(e.message)
                    }

                }
            }
            get("/admin/user/{userId}") {
                val userId = call.parameters["userId"]?.toLong()
                if (userId == null) {
                    call.badRequest()
                } else {
                    val query = GetUserInfoQuery(userId)
                    val result = queriesHandler.handle(query)
                    call.respondText(result)
                }
            }
        }
    }.start(wait = true)
    Unit
}

suspend fun ApplicationCall.badRequest(): Unit = respondText("Incorrect parameters", status = HttpStatusCode.BadRequest)

suspend fun ApplicationCall.error(errorMsg: String?): Unit =
    respondText("Error occurred: $errorMsg", status = HttpStatusCode.BadRequest)