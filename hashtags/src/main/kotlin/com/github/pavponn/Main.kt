package com.github.pavponn

import com.github.pavponn.utils.TimeUtils.Companion.SECONDS_IN_HOUR
import com.github.pavponn.vk.client.VkClientImpl
import com.github.pavponn.vk.manager.VkManagerImpl
import com.github.pavponn.vk.models.UrlConfig
import com.github.pavponn.vk.models.Version
import com.github.pavponn.vk.models.VkConfig
import com.natpryce.konfig.*
import kotlinx.coroutines.runBlocking
import java.lang.NumberFormatException
import java.time.Instant
import java.time.ZoneId

fun main(args: Array<String>) = runBlocking {
    require(args.size == 2) { "Invalid number of arguments" }
    val hashtag = args[0]
    val n: Int
    try {
        n = Integer.parseInt(args[1])
    } catch(e: NumberFormatException) {
        println("Invalid argument 'n'. Integer in range 1..24 expected")
        return@runBlocking
    }

    val vkConfig = VkConfig(
        config[Vk.service_key],
        UrlConfig(config[Vk.Url.schema], config[Vk.Url.host], config[Vk.Url.port]),
        Version(config[Vk.Version.major], config[Vk.Version.minor])
    )

    val vkManager = VkManagerImpl(VkClientImpl(vkConfig))
    val results = vkManager.getHashTagStatsForNHoursFromNow(hashtag, n)
    val time = Instant.now()
    for ((hour, stats) in results.withIndex()) {
        val startTime = time.minusSeconds((hour + 1) * SECONDS_IN_HOUR)
        val endTime = time.minusSeconds(hour * SECONDS_IN_HOUR)
        println("${stats.postsCount} posts made with #${stats.hashtag} " +
                "from ${startTime.atZone(ZoneId.systemDefault())} " +
                "to ${endTime.atZone(ZoneId.systemDefault())}")
    }
}