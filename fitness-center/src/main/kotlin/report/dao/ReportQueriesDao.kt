package report.dao

import common.model.MemberReport

interface ReportQueriesDao {

    suspend fun getMemberReports(): List<Pair<MemberReport, Long>>

    suspend fun getMemberEventsFromEvent(memberId: Long, fromEventId: Long): Pair<MemberReport, Long>
}