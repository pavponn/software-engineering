package report.command

import base.command.Command
import base.command.CommandsRouter
import base.command.UnknownCommandException
import report.store.ReportStore

class ReportCommandsRouter(private val reportStore: ReportStore): CommandsRouter {

    override suspend fun route(command: Command): String = when(command) {
        is UpdateStoreForMemberCommand -> {
            reportStore.updateStore(command.memberId)
            "Updated store"
        }
        else -> throw UnknownCommandException(command)
    }
}