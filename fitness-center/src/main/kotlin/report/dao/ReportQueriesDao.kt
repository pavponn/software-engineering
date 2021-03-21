package report.dao

import common.model.UserReport

interface ReportQueriesDao {

    suspend fun getUserReports(): List<Pair<UserReport, Long>>
}