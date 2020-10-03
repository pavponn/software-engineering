package com.github.pavponn.vk.manager

import com.github.pavponn.utils.TimeUtils
import com.github.pavponn.vk.client.VkClient
import com.github.pavponn.vk.client.VkClientImpl
import com.github.pavponn.vk.models.Stats
import java.time.Instant

/**
 * @author pavponn
 */
class VKManagerImpl(private val vkClient: VkClient) {

    public suspend fun getHashTagStatsForNHoursFromNow(hashtag: String, n: Int): List<Stats> {
        require(n in 1..24) { "N should be in 1..24 range" }
        require(hashtag.isNotEmpty()) { "Hashtag is empty" }
        val time = Instant.now()
        return IntRange(0, n - 1)
            .map { TimeUtils.nHoursBeforeNowInterval(time, it) }
            .map { vkClient.getStatByHashtagsForHourBeforeTime(hashtag, it.first, it.second) }
            .map { Stats(hashtag, it?.total_count ?: -1) }
            .toList()
    }

}