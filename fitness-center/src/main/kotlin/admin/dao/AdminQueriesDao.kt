package admin.dao

import base.model.Member

interface AdminQueriesDao {

    suspend fun getMemberInfo(memberId: Long): Member?
}