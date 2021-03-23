package base.clock

import java.time.Instant
import java.time.temporal.TemporalAmount
import java.util.concurrent.atomic.AtomicReference

/**
 * @author pavponn
 */
class ControlledClock(start: Instant) : Clock {
    private val currentTime = AtomicReference<Instant>(start)

    override fun now(): Instant {
        return currentTime.get()
    }

    fun add(time: TemporalAmount) {
        currentTime.updateAndGet { it.plus(time) }
    }

    fun subtract(time: TemporalAmount) {
        currentTime.updateAndGet { it.minus(time) }
    }

    fun setTime(time: Instant) {
        currentTime.set(time)
    }
}