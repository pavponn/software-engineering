package admin.command

import base.command.Command
import java.time.Instant

data class RenewSubscriptionCommand(val memberId: Long, val endTime: Instant): Command