package turnstile.dao

import com.github.jasync.sql.db.SuspendingConnection
import common.dao.AbstractDatabaseFitnessDao
import sql.SqlQueries
import java.time.Instant

class DatabaseTurnstileCommandsDao(private val connection: SuspendingConnection) : AbstractDatabaseFitnessDao(),
    TurnstileCommandsDao {

    private data class TurnstileEvent(val type: TurnstileEventType, val time: Instant)

    private enum class TurnstileEventType {
        ENTER,
        EXIT
    }

    override suspend fun enter(userId: Long, time: Instant) = connection.inTransaction {
        val (user, _) = getUser(it, userId)
        if (user == null) {
            throw NoSuchElementException("No such user with id=$userId")
        }
        check(time.isBefore(user.subscriptionTimeEnd)) { "User doesn't have a valid subscription" }
        val lastEvent = getLastEvent(it, userId).second
        check(lastEvent?.first?.type != TurnstileEventType.ENTER) { "User with id=$userId can't enter as they didn't exit before" }
        val newEventId = lastEvent?.second?.let { eventId -> eventId + 1 } ?: 0
        it.sendPreparedStatement(
            SqlQueries.addTurnstileEvent,
            listOf(userId, newEventId, TurnstileEventType.ENTER, time)
        )
        Unit
    }

    override suspend fun exit(userId: Long, time: Instant): Pair<Instant, Long> = connection.inTransaction {
        val lastEvent = getLastEvent(it, userId).second
        check(
            lastEvent != null
                    && lastEvent.first.type === TurnstileEventType.ENTER
        ) { "User id=$userId can't exit, as they didn't enter" }
        check(lastEvent.first.time.isBefore(time)) { "User can't exit before previous time" }
        val newEventId = lastEvent.second + 1
        it.sendPreparedStatement(
            SqlQueries.addTurnstileEvent,
            listOf(userId, newEventId, TurnstileEventType.EXIT, time)
        )

        Pair(lastEvent.first.time, 10)
    }

    private suspend fun getLastEvent(
        transaction: SuspendingConnection,
        userId: Long
    ): Pair<String?, Pair<TurnstileEvent, Long>?> {
        val result = transaction.sendPreparedStatement(SqlQueries.getTurnstileEventsByUserId, listOf(userId)).rows
        if (result.isEmpty()) {
            return Pair(null, null)
        }
        val name = result[0].getString("name")!!
        val id = result[0].getLong("eventId") ?: return Pair(name, null)
        val type = TurnstileEventType.valueOf(result[0].getString("eventType")!!)
        val time = result[0].getAs<Instant>("eventTime")
        return Pair(name, Pair(TurnstileEvent(type, time), userId))
    }
}