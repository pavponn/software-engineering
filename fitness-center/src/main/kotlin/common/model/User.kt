package common.model

import java.time.Instant

data class User(val id: Long, val name: String, val subscriptionTimeEnd: Instant?)
