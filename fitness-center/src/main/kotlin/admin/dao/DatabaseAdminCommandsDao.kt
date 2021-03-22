package admin.dao

import com.github.jasync.sql.db.SuspendingConnection
import common.dao.AbstractDatabaseFitnessDao
import sql.SqlQueries
import java.time.Instant
import java.util.concurrent.atomic.AtomicReference

class DatabaseAdminCommandsDao(private val connection: SuspendingConnection) : AbstractDatabaseFitnessDao(),
    AdminCommandsDao {

    data class IdInfo(val maxUsedId: Long, val maxId: Long)

    private val idInfoRef: AtomicReference<IdInfo> = AtomicReference(IdInfo(-1, -1))

    override suspend fun addMember(name: String): Long = connection.inTransaction {
        val newId = getId(it)
        it.sendPreparedStatement(SqlQueries.addUser, listOf(newId, name))
        newId
    }

    override suspend fun renewSubscription(memberId: Long, endTime: Instant) = connection.inTransaction {
        val (_, eventId) = getMember(it, memberId)
        val newEventId = (eventId ?: 0) + 1
        it.sendPreparedStatement(SqlQueries.renewSubscription, listOf(memberId, newEventId, endTime))
        Unit
    }

    private suspend fun getId(transaction: SuspendingConnection): Long {
        while (true) {
            val idInfo = idInfoRef.get()
            if (idInfo.maxUsedId == idInfo.maxId) {
                val curMaxId = if (idInfo.maxUsedId == -1L) {
                    val rows = transaction.sendPreparedStatement(SqlQueries.getMaxUserId).rows
                    rows[0].getLong("maxid")!!
                } else {
                    idInfo.maxId
                }
                val nextMaxId = curMaxId + 100
                transaction.sendPreparedStatement(SqlQueries.updateMaxIdForUsers, listOf(nextMaxId, curMaxId))
                if (idInfoRef.compareAndSet(idInfo, IdInfo(curMaxId + 1, nextMaxId))) {
                    return curMaxId + 1
                }
            } else {
                val resultId = idInfo.maxUsedId + 1
                if (idInfoRef.compareAndSet(idInfo, IdInfo(resultId, idInfo.maxId))) {
                    return resultId
                }
            }
        }
    }
}