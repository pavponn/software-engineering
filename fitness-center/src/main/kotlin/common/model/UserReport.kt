package common.model

import java.time.Duration

data class UserReport(val userId: Long, val totalVisits: Long, val totalTime: Duration)

