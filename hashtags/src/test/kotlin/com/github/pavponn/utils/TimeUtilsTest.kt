package com.github.pavponn.utils

import org.junit.Assert
import org.junit.Test
import java.lang.IllegalArgumentException
import java.time.Instant

class TimeUtilsTest {
    companion object {
        const val HOURS_TO_SECS = 60L * 60L
    }

    @Test
    fun `should calculate valid period for previous hour`() {
        val n = 0
        val initHours = 10
        val time = Instant.EPOCH.plusSeconds(initHours * HOURS_TO_SECS)
        val (from, to) = TimeUtils.nHoursBeforeNowInterval(time, n)
        val (expectedFrom, expectedTo) = getExpectedFromAndTo(initHours, n)
        Assert.assertEquals(expectedFrom, from)
        Assert.assertEquals(expectedTo, to)
    }

    @Test
    fun `should calculate valid period for one hour before`() {
        val n = 1
        val initHours = 5
        val time = Instant.EPOCH.plusSeconds(initHours * HOURS_TO_SECS)
        val (from, to) = TimeUtils.nHoursBeforeNowInterval(time, n)
        val (expectedFrom, expectedTo) = getExpectedFromAndTo(initHours, n)
        Assert.assertEquals(expectedFrom, from)
        Assert.assertEquals(expectedTo, to)
    }


    @Test(expected = IllegalArgumentException::class)
    fun `should throw IllegalArgumentException if n is negative`() {
        val n = -1
        val initHours = 7
        val time = Instant.EPOCH.plusSeconds(initHours * HOURS_TO_SECS)
        TimeUtils.nHoursBeforeNowInterval(time, n)
    }

    private fun getExpectedFromAndTo(initHours: Int, n: Int): Pair<Long, Long> {
        val expectedFrom = Instant.EPOCH.plusSeconds((initHours - n - 1) * HOURS_TO_SECS).epochSecond
        val expectedTo = Instant.EPOCH.plusSeconds((initHours - n) * HOURS_TO_SECS).epochSecond
        return Pair(expectedFrom, expectedTo)
    }

}