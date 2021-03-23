package turnstile.command

import base.command.Command
import java.time.Instant

data class EnterCommand(val memberId: Long, val time: Instant): Command