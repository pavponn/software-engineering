package common.command

class UnknownCommandException(command: Command) : Exception("Unknown command $command")