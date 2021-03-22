package admin

import admin.command.AddMemberCommand
import admin.command.AdminCommandsHandler
import admin.command.RenewSubscriptionCommand
import admin.dao.DatabaseAdminCommandsDao
import admin.dao.DatabaseAdminQueriesDao
import admin.query.AdminQueriesHandler
import admin.query.GetMemberInfoQuery
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
            post("/admin/member") {
                val name = call.request.queryParameters["name"]
                if (name == null) {
                    call.badRequest()
                } else {
                    try {
                        val command = AddMemberCommand(name)
                        val result = commandsHandler.handle(command)
                        call.respondText(result)
                    } catch (e: Exception) {
                        call.error(e.message)
                    }

                }
            }
            post("/admin/subscription") {
                val memberId = call.request.queryParameters["memberId"]?.toLong()
                val endTimeString = call.request.queryParameters["endTime"]
                if (memberId == null || endTimeString == null) {
                    call.badRequest()
                } else {
                    try {
                        val endTime = Instant.parse(endTimeString)
                        val command = RenewSubscriptionCommand(memberId, endTime)
                        val result = commandsHandler.handle(command)
                        call.respondText(result)
                    } catch (e: Exception) {
                        call.error(e.message)
                    }

                }
            }
            get("/admin/member/{memberId}") {
                val memberId = call.parameters["memberId"]?.toLong()
                if (memberId == null) {
                    call.badRequest()
                } else {
                    try {
                        val query = GetMemberInfoQuery(memberId)
                        val result = queriesHandler.handle(query)
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