package base.command

class UnknownCommandException(command: Command) : Exception("Unknown command $command")