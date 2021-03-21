package turnstile.command

import common.command.Command
import java.time.Instant

data class ExitCommand(val userId: Long, val time: Instant): Command