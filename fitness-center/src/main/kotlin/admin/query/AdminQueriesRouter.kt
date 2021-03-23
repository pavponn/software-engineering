package admin.query

import admin.dao.AdminQueriesDao
import base.query.QueriesRouter
import base.query.Query
import base.query.UnknownQueryException

class AdminQueriesRouter(private val dao: AdminQueriesDao): QueriesRouter {



    override suspend fun route(query: Query) = when(query) {
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