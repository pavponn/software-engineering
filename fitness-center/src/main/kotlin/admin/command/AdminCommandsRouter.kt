package admin.command

import admin.dao.AdminCommandsDao
import base.command.Command
import base.command.CommandsRouter
import base.command.UnknownCommandException

class AdminCommandsRouter(private val dao: AdminCommandsDao) : CommandsRouter {

    override suspend fun route(command: Command): String = when (command) {
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