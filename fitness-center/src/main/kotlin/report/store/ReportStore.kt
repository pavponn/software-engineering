package report.store

import common.model.UserReport
import java.time.Instant

interface ReportStore {

    suspend fun initializeStore()

    fun getUserReport(userId: Long): UserReport?

    fun addVisit(userId: Long, startTime: Instant, endTime: Instant, exitEventId: Long)
}