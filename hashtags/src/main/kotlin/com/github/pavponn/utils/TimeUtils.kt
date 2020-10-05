package com.github.pavponn.utils

import java.time.Instant

/**
 * @author pavponn
 */
class TimeUtils {
    companion object {
        public const val SECONDS_IN_HOUR = 60L * 60L

        fun nHoursBeforeNowInterval(time: Instant, n: Int): Pair<Long, Long> {
            require(n >= 0)
            val startTime = time.minusSeconds((n + 1) * SECONDS_IN_HOUR)
            val endTime = time.minusSeconds(n * SECONDS_IN_HOUR)
            return Pair(startTime.epochSecond, endTime.epochSecond)
        }
    }

}