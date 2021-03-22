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
import report.command.ReportCommandsHandler
import report.command.UpdateStoreForMemberCommand
import report.dao.DatabaseReportQueriesDao
import report.query.GetMemberStatsQuery
import report.query.ReportQueriesHandler
import report.store.InMemoryReportStore
import kotlin.Exception

fun main(): Unit = runBlocking {
    val clock: Clock = NormalClock()
    val connection = PostgresConnection.getConnection()
    val queriesDao = DatabaseReportQueriesDao(connection)
    val reportStore = InMemoryReportStore(queriesDao)
    val commandsHandler = ReportCommandsHandler(reportStore)
    val queriesHandler = ReportQueriesHandler(reportStore)
    try {
        reportStore.initializeStore()
    } catch (e: Exception) {
        println(e.message)
    }

    embeddedServer(Netty, port = 2020) {
        routing {
            get("/report/stats/{memberId}") {
                val memberId = call.parameters["memberId"]?.toLong()
                if (memberId == null) {
                    call.badRequest()
                } else {
                    try {
                        val query = GetMemberStatsQuery(memberId)
                        val result = queriesHandler.handle(query)
                        call.respondText(result)
                    } catch (e: Exception) {
                        call.error(e.message)
                    }
                }
            }
            post("/report/update/{memberId}") {
                val memberId = call.parameters["memberId"]?.toLong()
                if (memberId == null) {
                    call.badRequest()
                } else {
                    try {
                        val command = UpdateStoreForMemberCommand(memberId)
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
