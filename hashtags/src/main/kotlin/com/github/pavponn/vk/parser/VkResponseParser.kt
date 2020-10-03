package com.github.pavponn.vk.parser

import com.github.pavponn.vk.models.Response

interface VkResponseParser {
    fun parse(response: String): Response
}