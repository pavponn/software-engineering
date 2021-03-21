package report.command

import common.command.Command
import java.time.Instant

data class AddVisitCommand(val userId: Long, val startTime: Instant, val endTime: Instant, val exitEventId: Long) :
    Command