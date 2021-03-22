package report.command

import common.command.Command
import common.command.CommandsHandler
import common.command.UnknownCommandException
import report.store.ReportStore

class ReportCommandsHandler(private val reportStore: ReportStore): CommandsHandler {
    override suspend fun handle(command: Command): String = when(command) {
        is UpdateStoreForMemberCommand -> {
            reportStore.updateStore(command.memberId)
            "Updated store"
        }
        else -> throw UnknownCommandException(command)
    }
}