package admin.dao

import com.github.jasync.sql.db.SuspendingConnection
import base.dao.AbstractDatabaseFitnessDao

class DatabaseAdminQueriesDao(private val connection: SuspendingConnection) : AdminQueriesDao,
    AbstractDatabaseFitnessDao() {

    override suspend fun getMemberInfo(memberId: Long) = connection.inTransaction {
        getMember(it, memberId).first
    }
}