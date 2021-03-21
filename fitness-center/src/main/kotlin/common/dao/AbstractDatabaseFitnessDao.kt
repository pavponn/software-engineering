package common.dao

import com.github.jasync.sql.db.SuspendingConnection
import common.model.User
import sql.SqlQueries
import java.time.Instant

abstract class AbstractDatabaseFitnessDao {

    protected suspend fun getUser(transaction: SuspendingConnection, userId: Long): Pair<User?, Long?> {
        val result = transaction.sendPreparedStatement(SqlQueries.getUser, listOf(userId)).rows
        return if (result.isEmpty()) {
            Pair(null, null)
        } else {
            val name = result[0].getString("name")!!
            val subscriptionEndTime = result[0].getAs<Instant>("endTime")
            val eventId = result[0].getLong("eventId")
            Pair(User(userId, name, subscriptionEndTime), eventId)
        }
    }
}