package common.connection

import com.github.jasync.sql.db.SuspendingConnection

interface Connection {

    fun getConnection(): SuspendingConnection
}