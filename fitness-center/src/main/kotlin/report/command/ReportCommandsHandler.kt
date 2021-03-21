package report.command

import common.command.Command
import common.command.CommandsHandler
import common.command.UnknownCommandException
import report.dao.ReportCommandsDao

class ReportCommandsHandler(private val dao: ReportCommandsDao): CommandsHandler {
    override suspend fun handle(command: Command): String = when(command) {
        is AddVisitCommand -> {
            "1"
        }
        else -> throw UnknownCommandException(command)
    }
}