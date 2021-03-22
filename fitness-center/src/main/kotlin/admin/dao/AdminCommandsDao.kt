package admin.dao

import java.time.Instant

interface AdminCommandsDao {

    suspend fun addMember(name: String): Long

    suspend fun renewSubscription(memberId: Long, endTime: Instant)
}