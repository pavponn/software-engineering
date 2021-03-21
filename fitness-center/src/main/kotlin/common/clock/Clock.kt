package common.clock

import java.time.Instant

/**
 * @author pavponn
 */
interface Clock {
    fun now(): Instant
}