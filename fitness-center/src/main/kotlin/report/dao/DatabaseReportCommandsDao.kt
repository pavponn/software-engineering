package report.dao

import com.github.jasync.sql.db.SuspendingConnection

class DatabaseReportCommandsDao(private val connection: SuspendingConnection): ReportCommandsDao {
}