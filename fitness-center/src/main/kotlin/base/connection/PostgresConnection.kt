package base.connection

import com.github.jasync.sql.db.SuspendingConnection
import com.github.jasync.sql.db.asSuspending
import com.github.jasync.sql.db.postgresql.PostgreSQLConnectionBuilder

object PostgresConnection : Connection {
    override fun getConnection(): SuspendingConnection {
        return PostgreSQLConnectionBuilder.createConnectionPool {
            host = "localhost"
            database = "fitness"
            username = "pavel"
            port = 5432
        }.asSuspending
    }
}