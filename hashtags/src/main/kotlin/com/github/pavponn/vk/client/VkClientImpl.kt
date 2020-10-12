package com.github.pavponn.vk.client

import com.github.pavponn.url.UrlCreator
import com.github.pavponn.url.UrlReader
import com.github.pavponn.vk.models.Response
import com.github.pavponn.vk.models.VkConfig
import com.github.pavponn.vk.parser.VkResponseParser
import com.github.pavponn.vk.parser.VkResponseParserImpl
import kotlinx.coroutines.withTimeout
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * @author pavponn
 */
class VkClientImpl(private val vkConfig: VkConfig) : VkClient {
    private val parser: VkResponseParser = VkResponseParserImpl()
    private val urlReader: UrlReader = UrlReader()
    private val urlCreator: UrlCreator = UrlCreator()

    companion object {
        const val NEWS_FEED_SEARCH_METHOD = "/method/newsfeed.search"
    }

    public override suspend fun getStatByHashtagsForHourBeforeTime(hashtag: String, startTime: Long, endTime: Long): Response? {
        val responseString = urlReader.readAsString(createHashTagsStatsUrlString(hashtag, startTime, endTime))
        return try {
            withTimeout(vkConfig.timeout.toMillis()) {
                parser.parse(responseString)
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun createHashTagsStatsUrlString(hashtag: String, startTime: Long, endTime: Long): String {
        val baseUrl = "${vkConfig.urlConfig.schema}://${vkConfig.urlConfig.host}$NEWS_FEED_SEARCH_METHOD"
        val encodedHashtag = URLEncoder.encode(hashtag, StandardCharsets.UTF_8)
        val params = mapOf(
            Pair("access_token", vkConfig.accessToken),
            Pair("start_time", "$startTime"),
            Pair("end_time", "$endTime"),
            Pair("count", "0"),
            Pair("v", "${vkConfig.version.major}.${vkConfig.version.minor}"),
            Pair("q", "%23$encodedHashtag")
        )

        return urlCreator.getUrlString(baseUrl, params)
    }

}