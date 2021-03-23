package base.clock

import java.time.Instant

/**
 * @author pavponn
 */
interface Clock {
    fun now(): Instant
}