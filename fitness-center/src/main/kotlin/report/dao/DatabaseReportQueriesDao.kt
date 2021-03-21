package report.dao

import com.github.jasync.sql.db.SuspendingConnection
import common.model.UserReport
import sql.SqlQueries
import java.time.Duration


class DatabaseReportQueriesDao(private val connection: SuspendingConnection) : ReportQueriesDao {
    override suspend fun getUserReports(): List<Pair<UserReport, Long>> {
        val reports = ArrayList<Pair<UserReport, Long>>()
        val stats = connection.sendPreparedStatement(SqlQueries.getUserReports).rows
        for (row in stats) {
            val userId = row.getLong("userId")!!
            val totalVisits = row.getLong("totalVisits")!!
            val totalTime = row.getAs<Duration>("totalTime")
            val lastExitEventId = row.getLong("lastExitId")!!
            reports.add(UserReport(userId, totalVisits, totalTime) to lastExitEventId)
        }
        return reports
    }


}