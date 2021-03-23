package report

import com.github.jasync.sql.db.QueryResult
import com.github.jasync.sql.db.ResultSet
import com.github.jasync.sql.db.RowData
import com.github.jasync.sql.db.SuspendingConnection
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.joda.time.LocalDateTime
import base.sql.SqlQueries
import org.joda.time.Period

fun mockkInitializeWithNoData(connection: SuspendingConnection) {
    val rows = mockk<ResultSet>()
    val rowDataIterator = mockk<Iterator<RowData>>()
    every { rowDataIterator.hasNext() }.returns(false)
    every { rows.iterator() }.returns(rowDataIterator)

    coEvery {
        connection.sendPreparedStatement(SqlQueries.getMembersReports)
    }.returns(QueryResult(0, "OK", rows))
}

fun mockkInitializeWithData(
    connection: SuspendingConnection,
    memberId: Long,
    totalVisits: Long,
    totalTime: Period,
    maxExitId: Long
) {
    val rows = mockk<ResultSet>()
    val rowDataIterator = mockk<Iterator<RowData>>()
    val row = mockk<RowData>()

    every { row.getLong("userid") }.returns(memberId)
    every { row.getLong("totalvisits") }.returns(totalVisits)
    every { row.getAs<Period>("totaltime") }.returns(totalTime)
    every { row.getLong("lastexitid") }.returns(maxExitId)

    every { rowDataIterator.hasNext() }.returns(true).andThen(false)
    every { rowDataIterator.next() }.returns(row)
    every { rows.iterator() }.returns(rowDataIterator)

    coEvery {
        connection.sendPreparedStatement(SqlQueries.getMembersReports)
    }.returns(QueryResult(0, "OK", rows))
}

fun mockkUpdateStoreOnce(
    connection: SuspendingConnection,
    memberId: Long,
    fromEventId: Long,
    eventIdEnter: Long,
    eventIdExit: Long,
    eventTimeEnter: LocalDateTime,
    eventTimeExit: LocalDateTime
) {
    val rows = mockk<ResultSet>()
    val rowDataIterator = mockk<Iterator<RowData>>()
    val row = mockk<RowData>()

    every { row.getLong("eventid") }.returns(eventIdEnter).andThen(eventIdExit)
    every { row.getString("eventtype") }.returns("ENTER").andThen("EXIT")
    every { row.getAs<LocalDateTime>("eventtime") }.returns(eventTimeEnter).andThen(eventTimeExit)

    every { rowDataIterator.hasNext() }.returns(true).andThen(true).andThen(false)
    every { rowDataIterator.next() }.returns(row).andThen(row)
    every { rows.iterator() }.returns(rowDataIterator)

    coEvery {
        connection.sendPreparedStatement(SqlQueries.getUserEventsNew, listOf(memberId, fromEventId))
    }.returns(QueryResult(0, "OK", rows))
}