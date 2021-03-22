package report.store

import common.model.MemberReport
import java.time.Instant

interface ReportStore {

    suspend fun initializeStore()

    suspend fun getMemberReport(memberId: Long): MemberReport?

    suspend fun updateStore(memberId: Long)
}