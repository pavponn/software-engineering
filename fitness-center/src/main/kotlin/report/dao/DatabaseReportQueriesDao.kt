package report.dao

import com.github.jasync.sql.db.SuspendingConnection
import common.model.MemberReport
import common.utils.toInstant
import common.utils.toJDuration
import org.joda.time.LocalDateTime
import sql.SqlQueries
import java.time.Duration
import java.time.Instant
import kotlin.math.max


class DatabaseReportQueriesDao(private val connection: SuspendingConnection) : ReportQueriesDao {

    override suspend fun getMemberReports(): List<Pair<MemberReport, Long>> {
        val reports = ArrayList<Pair<MemberReport, Long>>()
        val stats = connection.sendPreparedStatement(SqlQueries.getUserReports).rows
        for (row in stats) {
            val userId = row.getLong("userid")!!
            val totalVisits = row.getLong("totalvisits")!!
            val totalTime = row.getAs<org.joda.time.Period>("totaltime").toJDuration()
            val lastExitEventId = row.getLong("lastexitid")!!
            reports.add(MemberReport(userId, totalVisits, totalTime) to lastExitEventId)
        }
        return reports
    }

    private data class TurnstileEvent(val type: TurnstileEventType, val time: Instant, val id: Long)

    private enum class TurnstileEventType {
        ENTER,
        EXIT
    }

    override suspend fun getMemberEventsFromEvent(memberId: Long, fromEventId: Long): Pair<MemberReport, Long> {
            val newEvents = connection.sendPreparedStatement(SqlQueries.getUserEventsNew, listOf(memberId, fromEventId)).rows
            var maxExitEventId = -1L
            val eventsList = ArrayList<TurnstileEvent>()
            for (row in newEvents) {
                val eventId = row.getLong("eventid")!!
                val eventType = TurnstileEventType.valueOf(row.getString("eventtype")!!)
                val eventTime = row.getAs<LocalDateTime>("eventtime").toInstant()
                if (eventType == TurnstileEventType.EXIT) {
                    maxExitEventId = max(maxExitEventId, eventId)
                }
                eventsList.add(TurnstileEvent(eventType, eventTime, eventId))
            }
            val eventsListFiltered =
                eventsList.filter { event -> event.id <= maxExitEventId }.sortedBy { event -> event.time }
            var totalDuration = Duration.ZERO
            var i = 0
            check(eventsListFiltered.size % 2 == 0) { "Events list is in wrong state" }
            while (i < eventsListFiltered.size) {
                totalDuration =
                    totalDuration.plus(Duration.between(eventsListFiltered[i].time, eventsListFiltered[i + 1].time))
                i += 2
            }

            return Pair(MemberReport(memberId, (eventsListFiltered.size / 2).toLong(), totalDuration), maxExitEventId)
    }


}