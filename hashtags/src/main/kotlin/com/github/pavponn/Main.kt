package com.github.pavponn

import com.github.pavponn.vk.client.VkClientImpl
import com.github.pavponn.vk.manager.VKManagerImpl
import com.github.pavponn.vk.models.UrlConfig
import com.github.pavponn.vk.models.Version
import com.github.pavponn.vk.models.VkConfig
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val token = "6368fee36368fee36368fee356631cc778663686368fee33c10af03906314ac730e8817"
    val vkConfig = VkConfig(
        token,
        UrlConfig("https", "api.vk.com", 443),
        Version(5, 124)
    )
    val vkManager = VKManagerImpl(VkClientImpl(vkConfig))
    vkManager.getHashTagStatsForNHoursFromNow("лол", 12).forEach(::println)
}