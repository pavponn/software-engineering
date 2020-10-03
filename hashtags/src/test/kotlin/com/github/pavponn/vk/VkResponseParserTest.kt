package com.github.pavponn.vk

import com.github.pavponn.exceptions.ResponseParserException
import com.github.pavponn.vk.parser.VkResponseParser
import com.github.pavponn.vk.parser.VkResponseParserImpl
import org.jetbrains.annotations.TestOnly
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * @author pavponn
 */
class VkResponseParserTest {
    private lateinit var vkResponseParser: VkResponseParser

    @Before
    fun setUp() {
        vkResponseParser = VkResponseParserImpl();
    }

    @Test
    fun `should parse correct response`() {
        val responseString = "{\"response\":{\"count\":20,\"total_count\":20}}"
        val response = vkResponseParser.parse(responseString)
        Assert.assertEquals(response.count, 20)
        Assert.assertEquals(response.total_count, 20)
    }

    @Test
    fun `should parse correct response and skip unknown keys`() {
        val responseString = "{\"response\":{\"items\":[],\"count\":6,\"total_count\":7}}"
        val response = vkResponseParser.parse(responseString)
        Assert.assertEquals(response.count, 6)
        Assert.assertEquals(response.total_count, 7)
    }

    @Test(expected = ResponseParserException::class)
    fun `should throw when count field is not presented`() {
        val responseString = "{\"response\":{\"total_count\":7}}"
        vkResponseParser.parse(responseString)
    }

    @Test(expected = ResponseParserException::class)
    fun `should throw when total_count field is not presented`() {
        val responseString = "{\"response\":{\"count\":7}}"
        vkResponseParser.parse(responseString)
    }

    @Test(expected = ResponseParserException::class)
    fun `should throw when no field is presented`() {
        val responseString = "{\"response\":{}}"
        vkResponseParser.parse(responseString)
    }

    @Test(expected = ResponseParserException::class)
    fun `should throw when response contains error`() {
        val responseString = "{\"error\":{\"message\":\"some error\"}}"
        vkResponseParser.parse(responseString)
    }
}