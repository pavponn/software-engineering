package admin.dao

import java.time.Instant

interface AdminCommandsDao {

    suspend fun addUser(name: String): Long

    suspend fun renewSubscription(userId: Long, endTime: Instant)
}