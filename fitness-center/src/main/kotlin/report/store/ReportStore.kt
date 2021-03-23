package report.store

import base.model.MemberReport

interface ReportStore {

    suspend fun initializeStore()

    suspend fun getMemberReport(memberId: Long): MemberReport?

    suspend fun updateStore(memberId: Long)
}