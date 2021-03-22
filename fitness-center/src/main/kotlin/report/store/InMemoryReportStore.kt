package report.store

import common.model.MemberReport
import report.dao.ReportQueriesDao
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.max

class InMemoryReportStore(private val dao: ReportQueriesDao) : ReportStore {
    private val store = ConcurrentHashMap<Long, Pair<MemberReport, Long>>()

    override suspend fun initializeStore() {
        val stats = dao.getMemberReports()
        for (data in stats) {
            store[data.first.memberId] = data
        }
    }

    override suspend fun updateStore(memberId: Long) {
        val fromEventId = if (store[memberId] == null) {
            -1
        } else {
            store[memberId]!!.second
        }
        val reportUpdate = dao.getMemberEventsFromEvent(memberId, fromEventId)
        if (store[memberId] == null) {
            store[memberId] = reportUpdate
        } else {
            val oldReport = store[memberId]!!
            var durationSum = Duration.ZERO
            durationSum = durationSum.plus(oldReport.first.totalTime)
            durationSum = durationSum.plus(reportUpdate.first.totalTime)
            store[memberId] =
                Pair(
                    MemberReport(
                        oldReport.first.memberId,
                        oldReport.first.totalVisits + reportUpdate.first.totalVisits,
                        durationSum
                    ),
                    max(oldReport.second, reportUpdate.second)
                )
        }
    }

    override suspend fun getMemberReport(memberId: Long): MemberReport? = store[memberId]?.first
}