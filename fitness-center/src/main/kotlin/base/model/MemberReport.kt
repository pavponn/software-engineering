package base.model

import java.time.Duration

data class MemberReport(val memberId: Long, val totalVisits: Long, val totalTime: Duration)

