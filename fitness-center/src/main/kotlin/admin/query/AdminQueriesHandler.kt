package admin.query

import admin.dao.AdminQueriesDao
import common.query.QueriesHandler
import common.query.Query
import common.query.UnknownQueryException

class AdminQueriesHandler(private val dao: AdminQueriesDao): QueriesHandler {
    override suspend fun handle(query: Query) = when(query) {
        is GetUserInfoQuery -> {
            val user = dao.getUserInfo(query.userId)
            if (user == null) {
                "No such user with id=${query.userId}"
            } else {
                "$user"
            }
        }
        else -> throw UnknownQueryException(query)

    }
}