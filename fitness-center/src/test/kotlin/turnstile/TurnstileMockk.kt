package turnstile

import base.model.TurnstileEventType
import base.sql.SqlQueries
import base.utils.toInstant
import com.github.jasync.sql.db.QueryResult
import com.github.jasync.sql.db.ResultSet
import com.github.jasync.sql.db.RowData
import com.github.jasync.sql.db.SuspendingConnection
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.joda.time.LocalDateTime
import java.time.Instant

fun mockkGetNonExistingTurnstileEvent(connection: SuspendingConnection, memberId: Long) {
    val rows = mockk<ResultSet>()
    every { rows.isEmpty() }.returns(true)

    coEvery {
        connection.sendPreparedStatement(SqlQueries.getTurnstileEventsByUserId, listOf(memberId))
    }.returns(QueryResult(0, "OK", rows))
}

fun mockkGetExistingTurnstileEvent(
    connection: SuspendingConnection,
    memberId: Long,
    memberName: String,
    eventId: Long,
    eventType: String,
    eventTime: LocalDateTime
) {
    val rows = mockk<ResultSet>()
    every { rows.isEmpty() }.returns(false)

    val row = mockk<RowData>()
    every { row.getString("name") }.returns(memberName)
    every { row.getLong("eventid") }.returns(eventId)
    every { row.getString("eventtype") }.returns(eventType)
    every { row.getAs<LocalDateTime?>("eventtime") }.returns(eventTime)
    every { rows[0] }.returns(row)

    coEvery {
        connection.sendPreparedStatement(SqlQueries.getTurnstileEventsByUserId, listOf(memberId))
    }.returns(QueryResult(0, "OK", rows))
}

fun mockkAddTurnstileEvent(
    connection: SuspendingConnection,
    memberId: Long,
    eventId: Long,
    eventType: TurnstileEventType,
    eventTime: Instant
) {
    coEvery {
        connection.sendPreparedStatement(SqlQueries.addTurnstileEvent, listOf(memberId, eventId, eventType, eventTime))
    }.returns(QueryResult(0, "OK"))
}