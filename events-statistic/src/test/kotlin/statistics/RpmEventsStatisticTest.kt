package statistics

import clock.ControlledClock
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import java.time.Duration.*
import java.time.Instant

/**
 * @author pavponn
 */
class RpmEventsStatisticTest {

    companion object {
        private const val EVENT_1 = "event1"
        private const val EVENT_2 = "event2"
        private const val EVENT_3 = "event3"
        private const val RPM_1_60 = 1.0 / 60.0
        private const val RPM_2_60 = 2.0 / 60.0

    }

    private lateinit var eventsStatistic: EventsStatistic
    private lateinit var clock: ControlledClock

    @Before
    fun setUp() {
        clock = ControlledClock(Instant.now())
        eventsStatistic = RpmEventsStatistic(clock)
    }

    @Test
    fun simpleStatsTest() {
        eventsStatistic.incEvent(EVENT_1)
        eventsStatistic.incEvent(EVENT_2)

        assertEquals(
            eventsStatistic.getEventStatisticByName(EVENT_1),
            RPM_1_60,
            1e-5
        )
    }

    @Test
    fun simpleAllStatsTest() {
        eventsStatistic.incEvent(EVENT_1)
        eventsStatistic.incEvent(EVENT_1)
        eventsStatistic.incEvent(EVENT_2)

        assertEquals(
            eventsStatistic.getAllEventsStatistic(),
            mapOf(
                EVENT_1 to RPM_2_60,
                EVENT_2 to RPM_1_60
            )
        )
    }

    @Test
    fun noStatsAfterOneHourTest() {
        eventsStatistic.incEvent(EVENT_1)
        eventsStatistic.incEvent(EVENT_2)

        clock.add(ofHours(1))

        assertEquals(
            eventsStatistic.getAllEventsStatistic(),
            emptyMap<String, Double>()
        )
    }

    @Test
    fun onlyLastHourStatsTest() {
        eventsStatistic.incEvent(EVENT_1)
        eventsStatistic.incEvent(EVENT_2)

        clock.add(ofMinutes(30))

        eventsStatistic.incEvent(EVENT_1)
        eventsStatistic.incEvent(EVENT_2)

        clock.add(ofMinutes(40))

        assertEquals(
            eventsStatistic.getEventStatisticByName(EVENT_1),
            RPM_1_60,
            1e-5
        )
    }

    @Test
    fun onlyLastHourAllStatsTest() {
        eventsStatistic.incEvent(EVENT_1)
        eventsStatistic.incEvent(EVENT_2)
        eventsStatistic.incEvent(EVENT_1)

        clock.add(ofMinutes(20))

        eventsStatistic.incEvent(EVENT_2)
        eventsStatistic.incEvent(EVENT_3)
        eventsStatistic.incEvent(EVENT_3)
        eventsStatistic.incEvent(EVENT_2)
        eventsStatistic.incEvent(EVENT_1)

        clock.add(ofMinutes(50))

        assertEquals(
            eventsStatistic.getAllEventsStatistic(),
            mapOf(
                EVENT_3 to RPM_2_60,
                EVENT_2 to RPM_2_60,
                EVENT_1 to RPM_1_60
            )
        )
    }

}