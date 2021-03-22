package admin.dao

import common.model.Member

interface AdminQueriesDao {

    suspend fun getMemberInfo(memberId: Long): Member?
}