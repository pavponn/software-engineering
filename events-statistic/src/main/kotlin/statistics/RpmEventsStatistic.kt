package statistics

import clock.Clock
import java.time.Instant
import java.util.*
import java.time.temporal.ChronoUnit.HOURS

/**
 * @author pavponn
 */
class RpmEventsStatistic(private val clock: Clock) : EventsStatistic {

    private val events: Queue<Pair<String, Instant>> = ArrayDeque()
    private val eventsCounter: MutableMap<String, Int> = HashMap()

    override fun incEvent(name: String) {
        val time = clock.now()
        removeOutdatedEvents(time)
        events.add(Pair(name, time))
        eventsCounter.merge(name, 1, Integer::sum)
    }

    override fun getEventStatisticByName(name: String): Double {
        val requestTime = clock.now()
        removeOutdatedEvents(requestTime)
        return eventsCounter.getOrDefault(name, 0).toDouble() / MINUTES_PER_HOUR
    }

    override fun getAllEventsStatistic(): Map<String, Double> {
        val curTime = clock.now()
        removeOutdatedEvents(curTime)
        return eventsCounter.toMap().mapValues { it.value.toDouble() / MINUTES_PER_HOUR }
    }

    override fun printStatistic() {
        val stats = getAllEventsStatistic()
        for (key in stats) {
            print("${key.key} -> ${key.value}")
        }
    }

    private fun removeOutdatedEvents(time: Instant) {
        val shouldRemove: (Instant) -> Boolean = { HOURS.between(events.peek().second, it) >= 1 }
        while (events.size > 0 && shouldRemove(time)) {
            val nameAndTime = events.poll()
            val counter = eventsCounter[nameAndTime.first]!!
            eventsCounter[nameAndTime.first] = counter - 1
            if (eventsCounter[nameAndTime.first]!! <= 0) {
                eventsCounter.remove(nameAndTime.first)
            }
        }
    }

    companion object {
        private val MINUTES_PER_HOUR = HOURS.duration.toMinutes().toDouble()
    }
}