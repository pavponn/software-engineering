package turnstile.dao

import java.time.Instant

interface TurnstileCommandsDao {

    suspend fun enter(userId: Long, time: Instant)

    suspend fun exit(userId: Long, time: Instant): Pair<Instant, Long>
}