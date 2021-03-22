package common.utils

import org.joda.time.Duration
import org.joda.time.LocalDateTime
import org.joda.time.Period
import java.time.Instant
import java.util.*

fun LocalDateTime.toInstant(): Instant {
    val cal = Calendar.getInstance()
    cal.time = this.toDate()
    return cal.time.toInstant()
}

fun Period.toJDuration(): java.time.Duration {
    return java.time.Duration.ofMillis(this.toStandardDuration().millis)
}