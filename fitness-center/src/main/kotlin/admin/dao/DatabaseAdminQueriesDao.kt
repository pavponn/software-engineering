package admin.dao

import com.github.jasync.sql.db.SuspendingConnection
import common.dao.AbstractDatabaseFitnessDao

class DatabaseAdminQueriesDao(private val connection: SuspendingConnection) : AdminQueriesDao, AbstractDatabaseFitnessDao() {

    override suspend fun getUserInfo(userId: Long) =  connection.inTransaction {
        getUser(it, userId).first
    }
}