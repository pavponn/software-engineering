package com.github.pavponn.vk.client

import com.github.pavponn.vk.models.Response

interface VkClient {
    suspend fun getStatByHashtagsForHourBeforeTime(hashtag: String, startTime: Long, endTime: Long): Response?
}