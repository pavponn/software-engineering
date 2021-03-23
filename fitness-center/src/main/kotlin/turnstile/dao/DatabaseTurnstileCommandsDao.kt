package turnstile.dao

import com.github.jasync.sql.db.SuspendingConnection
import base.dao.AbstractDatabaseFitnessDao
import base.model.TurnstileEvent
import base.model.TurnstileEventType
import base.utils.toInstant
import org.joda.time.LocalDateTime
import base.sql.SqlQueries
import java.time.Instant

class DatabaseTurnstileCommandsDao(private val connection: SuspendingConnection) : AbstractDatabaseFitnessDao(),
    TurnstileCommandsDao {

    override suspend fun enter(memberId: Long, time: Instant) = connection.inTransaction {
        val (member, _) = getMember(it, memberId)
        if (member == null) {
            throw NoSuchElementException("No such member with id=$memberId")
        }
        check(time.isBefore(member.subscriptionTimeEnd)) { "Member doesn't have a valid subscription" }
        val lastEvent = getLastEvent(it, memberId).second
        check(lastEvent?.first?.type != TurnstileEventType.ENTER) { "Member with id=$memberId can't enter as they didn't exit before" }
        val newEventId = lastEvent?.second?.let { eventId -> eventId + 1 } ?: 0
        it.sendPreparedStatement(
            SqlQueries.addTurnstileEvent,
            listOf(memberId, newEventId, TurnstileEventType.ENTER, time)
        )
        Unit
    }

    override suspend fun exit(memberId: Long, time: Instant): Unit = connection.inTransaction {
        val lastEvent = getLastEvent(it, memberId).second
        check(
            lastEvent != null
                    && lastEvent.first.type === TurnstileEventType.ENTER
        ) { "Member id=$memberId can't exit, as they didn't enter" }
        check(lastEvent.first.time.isBefore(time)) { "Member can't exit before previous time" }
        val newEventId = lastEvent.second + 1
        it.sendPreparedStatement(
            SqlQueries.addTurnstileEvent,
            listOf(memberId, newEventId, TurnstileEventType.EXIT, time)
        )
        Unit
    }

    private suspend fun getLastEvent(
        transaction: SuspendingConnection,
        memberId: Long
    ): Pair<String?, Pair<TurnstileEvent, Long>?> {
        val result = transaction.sendPreparedStatement(SqlQueries.getTurnstileEventsByUserId, listOf(memberId)).rows
        if (result.isEmpty()) {
            return Pair(null, null)
        }
        val name = result[0].getString("name")!!
        val id = result[0].getLong("eventid") ?: return Pair(name, null)
        val type = TurnstileEventType.valueOf(result[0].getString("eventtype")!!)
        val time = result[0].getAs<LocalDateTime>("eventtime").toInstant()
        return Pair(name, Pair(TurnstileEvent(type, time), id))
    }
}