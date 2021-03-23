package base.model

import java.time.Instant

data class TurnstileEvent(val type: TurnstileEventType, val time: Instant)

data class TurnstileEventWithId(val type: TurnstileEventType, val time: Instant, val id: Long)

enum class TurnstileEventType {
    ENTER,
    EXIT
}
