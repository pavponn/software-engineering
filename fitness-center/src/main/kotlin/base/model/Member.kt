package base.model

import base.utils.DateTimeFormatter
import java.time.Instant

data class Member(val id: Long, val name: String, val subscriptionTimeEnd: Instant?) {

    override fun toString(): String {
        val time = if (subscriptionTimeEnd == null) {
            "N/A"
        } else {
            DateTimeFormatter.format(subscriptionTimeEnd)
        }
        return "Member(id=$id, name=$name, subscriptionEndTime=$time)"
    }
}
