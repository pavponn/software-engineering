package report.store

import com.github.jasync.sql.db.SuspendingConnection
import common.model.UserReport
import report.dao.ReportQueriesDao
import sql.SqlQueries
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.Period
import java.util.concurrent.ConcurrentHashMap

class InMemoryReportStore(private val dao: ReportQueriesDao) : ReportStore {
    private val store = ConcurrentHashMap<Long, Pair<UserReport, Long>>()

    override suspend fun initializeStore() {
        val stats = dao.getUserReports()
        for (data in stats) {
            store[data.first.userId] = data
        }
    }

    override fun getUserReport(userId: Long): UserReport? {
        return store[userId]?.first
    }

    override fun addVisit(userId: Long, startTime: Instant, endTime: Instant, exitEventId: Long) {
        val period = Duration.between(LocalDate.from(startTime), LocalDate.from(endTime))
        store.compute(userId) { _, data ->
            if (data == null) {
                (UserReport(userId, 1, period) to exitEventId)
            } else {
                val (report, lastExitEventId) = data
                if (exitEventId <= lastExitEventId) {
                    data
                } else {
                    val updatedReport = UserReport(userId, report.totalVisits + 1, report.totalTime + period)
                    Pair(updatedReport, exitEventId)
                }
            }
        }
    }
}