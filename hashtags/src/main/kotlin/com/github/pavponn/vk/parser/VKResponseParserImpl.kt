package com.github.pavponn.vk.parser

import com.github.pavponn.vk.models.Response
import com.github.pavponn.vk.models.VKResponse
import kotlinx.serialization.*
import kotlinx.serialization.json.*

/**
 * @author pavponn
 */
class VKResponseParser {

    fun parseResponse(response: String): Response? {
        return try {
            Json { ignoreUnknownKeys = true }.decodeFromString<VKResponse<Response>>(response).response
        } catch (e: Exception) {
            null
        }
    }
}