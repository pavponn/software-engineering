package admin.query

import admin.dao.AdminQueriesDao
import common.query.QueriesHandler
import common.query.Query
import common.query.UnknownQueryException

class AdminQueriesHandler(private val dao: AdminQueriesDao): QueriesHandler {



    override suspend fun handle(query: Query) = when(query) {
        is GetMemberInfoQuery -> {
            val member = dao.getMemberInfo(query.memberId)
            if (member == null) {
                "No such member with id=${query.memberId}"
            } else {
                "$member"
            }
        }
        else -> throw UnknownQueryException(query)

    }
}