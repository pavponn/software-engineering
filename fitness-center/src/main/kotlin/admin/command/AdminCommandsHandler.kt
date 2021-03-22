package admin.command

import admin.dao.AdminCommandsDao
import common.command.Command
import common.command.CommandsHandler
import common.command.UnknownCommandException

class AdminCommandsHandler(private val dao: AdminCommandsDao) : CommandsHandler {

    override suspend fun handle(command: Command): String = when (command) {
        is RenewSubscriptionCommand -> {
            dao.renewSubscription(command.memberId, command.endTime)
            "Renewed subscription for member with id= ${command.memberId}"
        }
        is AddMemberCommand -> {
            val id = dao.addMember(command.name)
            "Registered member `${command.name} with id=$id"
        }
        else -> throw UnknownCommandException(command)
    }
}