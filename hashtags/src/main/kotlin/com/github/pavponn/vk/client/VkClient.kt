package com.github.pavponn.vk.client

import com.github.pavponn.vk.models.Response

interface VkClient {
    fun getStatByHashtagsForHourBeforeTime(hashTag: String, startTime: Long, endTime: Long): Response?
}