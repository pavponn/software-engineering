package common.command

interface CommandsHandler {

    suspend fun handle(command: Command): String
}