package base.command

interface CommandsRouter {

    suspend fun route(command: Command): String
}