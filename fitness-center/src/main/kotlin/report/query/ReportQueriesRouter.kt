package report.query

import base.query.QueriesRouter
import base.query.Query
import base.query.UnknownQueryException
import report.store.ReportStore

class ReportQueriesRouter(private val reportStore: ReportStore) : QueriesRouter {

    override suspend fun route(query: Query): String = when (query) {
        is GetMemberStatsQuery -> {
            val report = reportStore.getMemberReport(query.memberId)
            if (report == null) {
                "No such member"
            } else {
                val averageVisitTime = report.totalTime.toSeconds() / (report.totalVisits * 1.0)
                val result = "Total time spent: ${report.totalTime.toSeconds()} seconds\n" +
                        "Total visits: ${report.totalVisits}\n" +
                        "Average visit time: $averageVisitTime seconds\n"
                result
            }
        }
        else -> throw UnknownQueryException(query)
    }
}