package report

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
import report.command.ReportCommandsRouter
import report.command.UpdateStoreForMemberCommand
import report.dao.DatabaseReportQueriesDao
import report.query.GetMemberStatsQuery
import report.query.ReportQueriesRouter
import report.store.InMemoryReportStore
import kotlin.Exception

fun main(): Unit = runBlocking {
    val clock: Clock = NormalClock()
    val connection = PostgresConnection.getConnection()
    val queriesDao = DatabaseReportQueriesDao(connection)
    val reportStore = InMemoryReportStore(queriesDao)
    val commandsHandler = ReportCommandsRouter(reportStore)
    val queriesHandler = ReportQueriesRouter(reportStore)
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
                        val result = queriesHandler.route(query)
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
