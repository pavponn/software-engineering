package report.query

import common.query.QueriesHandler
import common.query.Query
import common.query.UnknownQueryException
import report.store.ReportStore

class ReportQueriesHandler(private val reportStore: ReportStore) : QueriesHandler {

    override suspend fun handle(query: Query): String = when (query) {
        is GetUserStatsQuery -> {
            val report = reportStore.getUserReport(query.userId)
            if (report == null) {
                "No such user"
            } else {
                val averageVisitTime = report.totalTime.toMinutes() / (report.totalVisits * 1.0)
                "Total time spent: ${report.totalTime}\n" +
                        "Total visits: ${report.totalVisits}\n" +
                        "Average visit time: $averageVisitTime minutes\n"
            }
        }
        else -> throw UnknownQueryException(query)
    }
}