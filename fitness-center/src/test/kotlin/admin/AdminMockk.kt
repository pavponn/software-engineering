package admin


import com.github.jasync.sql.db.QueryResult
import com.github.jasync.sql.db.SuspendingConnection
import base.mockkGetExistingMember
import io.mockk.coEvery
import org.joda.time.LocalDateTime
import base.sql.SqlQueries
import java.time.Instant

fun mockkUpdateSubscription(
    connection: SuspendingConnection,
    memberId: Long,
    userName: String,
    userEndTime: Instant,
    subscribeTime: Instant,
    eventId: Long
) {
    mockkGetExistingMember(connection, memberId, userName, LocalDateTime(userEndTime.epochSecond), eventId)

    coEvery {
        connection.sendPreparedStatement(
            SqlQueries.renewSubscription,
            listOf(memberId, eventId + 1, subscribeTime)
        )
    }.returns(QueryResult(0, "OK"))
}