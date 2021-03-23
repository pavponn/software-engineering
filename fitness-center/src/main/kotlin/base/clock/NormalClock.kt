package base.clock

import java.time.Instant

/**
 * @author pavponn
 */
class NormalClock : Clock {
    override fun now(): Instant {
        return Instant.now()
    }

}