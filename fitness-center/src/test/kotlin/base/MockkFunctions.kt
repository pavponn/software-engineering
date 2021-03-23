package base

import com.github.jasync.sql.db.QueryResult
import com.github.jasync.sql.db.ResultSet
import com.github.jasync.sql.db.RowData
import com.github.jasync.sql.db.SuspendingConnection
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.joda.time.LocalDateTime
import base.sql.SqlQueries

fun mockkGetNonExistingMember(connection: SuspendingConnection, memberId: Long) {
    val rows = mockk<ResultSet>()
    every { rows.isEmpty() }.returns(true)

    coEvery {
        connection.sendPreparedStatement(SqlQueries.getUser, listOf(memberId))
    }.returns(QueryResult(0, "OK", rows))
}

fun mockkGetExistingMember(
    connection: SuspendingConnection,
    memberId: Long,
    userName: String,
    userEndTime: LocalDateTime,
    eventId: Long
) {
    val rows = mockk<ResultSet>()
    every { rows.isEmpty() }.returns(false)


    val row = mockk<RowData>()
    every { row.getString("name") }.returns(userName)
    every { row.getAs<LocalDateTime?>("endtime") }.returns(userEndTime)
    every { row.getLong("eventid") }.returns(eventId)
    every { rows[0] }.returns(row)

    coEvery {
        connection.sendPreparedStatement(SqlQueries.getUser, listOf(memberId))
    }.returns(QueryResult(0, "OK", rows))
}