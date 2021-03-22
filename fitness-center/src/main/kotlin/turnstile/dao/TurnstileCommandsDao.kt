package turnstile.dao

import java.time.Instant

interface TurnstileCommandsDao {

    suspend fun enter(memberId: Long, time: Instant)

    suspend fun exit(memberId: Long, time: Instant)
}