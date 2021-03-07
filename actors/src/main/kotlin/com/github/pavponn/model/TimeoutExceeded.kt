package com.github.pavponn.model

import scala.concurrent.duration.FiniteDuration

data class TimeoutExceeded(val duration: FiniteDuration) {}