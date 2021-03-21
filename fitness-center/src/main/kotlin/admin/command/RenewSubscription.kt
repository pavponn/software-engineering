package admin.command

import common.command.Command
import java.time.Instant

data class RenewSubscriptionCommand(val userId: Long, val endTime: Instant): Command