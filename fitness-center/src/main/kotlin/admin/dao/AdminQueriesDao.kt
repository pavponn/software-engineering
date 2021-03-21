package admin.dao

import common.model.User

interface AdminQueriesDao {

    suspend fun getUserInfo(userId: Long): User?
}