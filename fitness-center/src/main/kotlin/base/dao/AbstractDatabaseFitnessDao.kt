package base.dao

import com.github.jasync.sql.db.SuspendingConnection
import base.model.Member
import base.utils.toInstant
import org.joda.time.LocalDateTime
import base.sql.SqlQueries


abstract class AbstractDatabaseFitnessDao {

    protected suspend fun getMember(transaction: SuspendingConnection, memberId: Long): Pair<Member?, Long?> {
        val result = transaction.sendPreparedStatement(SqlQueries.getUser, listOf(memberId)).rows
        return if (result.isEmpty()) {
            Pair(null, null)
        } else {
            val name = result[0].getString("name")!!
            val subscriptionEndTime = result[0].getAs<LocalDateTime?>("endtime")?.toInstant()

            val eventId = result[0].getLong("eventid")
            Pair(Member(memberId, name, subscriptionEndTime), eventId)
        }
    }
}