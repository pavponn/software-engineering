package com.github.pavponn.vk.models

import kotlinx.serialization.*

@Serializable
data class Response(val count: Int, val total_count: Int)

@Serializable
class VKResponse<T>(val response: T)