package com.github.pavponn.vk.manager

import com.github.pavponn.vk.models.Stats

interface VkManager {
    suspend fun getHashTagStatsForNHoursFromNow(hashtag: String, n: Int): List<Stats>
}