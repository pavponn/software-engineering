package report

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
import report.command.AddVisitCommand
import report.command.ReportCommandsHandler
import report.dao.DatabaseReportCommandsDao
import report.dao.DatabaseReportQueriesDao
import report.query.GetUserStatsQuery
import report.query.ReportQueriesHandler
import report.store.InMemoryReportStore
import java.lang.Exception
import java.time.Instant

fun main(): Unit = runBlocking {
    val clock: Clock = NormalClock()
    val connection = PostgresConnection.getConnection()
    val queriesDao = DatabaseReportQueriesDao(connection)
    val reportStore = InMemoryReportStore(queriesDao)
    val queriesHandler = ReportQueriesHandler(reportStore)
    val commandsDao = DatabaseReportCommandsDao(connection)
    val commandsHandler = ReportCommandsHandler(commandsDao)
    embeddedServer(Netty, port = 2020) {
        routing {
            get("/report/stats/{userId}") {
                val userId = call.parameters["userId"]?.toLong()
                if (userId == null) {
                    call.badRequest()
                } else {
                    try {
                        val query = GetUserStatsQuery(userId)
                        val result = queriesHandler.handle(query)
                        call.respondText(result)
                    } catch (e: Exception) {
                        call.error(e.message)
                    }
                }
            }
            post("/report/visit") {
                val userId = call.request.queryParameters["userId"]?.toLong()
                val eventId = call.request.queryParameters["eventId"]?.toLong()
                val startTimeString = call.request.queryParameters["startTime"]
                val endTimeString = call.request.queryParameters["endTime"]
                if (userId == null || eventId == null || startTimeString == null || endTimeString == null) {
                    call.badRequest()
                } else {
                    try {
                        val startTime = Instant.parse(startTimeString)
                        val endTime = Instant.parse(startTimeString)
                        val command = AddVisitCommand(userId, startTime, endTime, eventId)
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
