package admin.command

import admin.dao.AdminCommandsDao
import common.command.Command
import common.command.CommandsHandler
import common.command.UnknownCommandException

class AdminCommandsHandler(private val dao: AdminCommandsDao) : CommandsHandler {

    override suspend fun handle(command: Command): String = when (command) {
        is RenewSubscriptionCommand -> {
            dao.renewSubscription(command.userId, command.endTime)
            "Renewed subscription for user with id= ${command.userId}"
        }
        is AddUserCommand -> {
            val id = dao.addUser(command.name)
            "Registered user `${command.name} with id=$id"
        }
        else -> throw UnknownCommandException(command)
    }
}