package com.github.pavponn.vk

import com.github.pavponn.vk.client.VkClient
import com.github.pavponn.vk.manager.VkManager
import com.github.pavponn.vk.manager.VkManagerImpl
import com.github.pavponn.vk.models.Response
import com.github.pavponn.vk.models.Stats
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.lang.IllegalArgumentException


/**
 * @author pavponn
 */
class VkManagerTest {
    private lateinit var vkManager: VkManager

    @Mock
    private lateinit var vkClient: VkClient


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        vkManager = VkManagerImpl(vkClient)
    }

    @Test
    fun `should return valid stats for 1 hour`(): Unit = runBlocking {
        val hashtag = "some_tag"
        mockClientFunc(hashtag, Response(42, 42))
        val result = vkManager.getHashTagStatsForNHoursFromNow(hashtag, 1)
        Assert.assertEquals(result.size, 1)
        Assert.assertEquals(result[0], Stats(hashtag, 42))
    }

    @Test
    fun `should return valid stats for multiple hours`(): Unit = runBlocking {
        val hashtag = "another_tag"
        mockClientFunc(hashtag, Response(2, 2))
        val result = vkManager.getHashTagStatsForNHoursFromNow(hashtag, 4)
        val elem = Stats(hashtag, 2)
        Assert.assertEquals(result.size, 4)
        Assert.assertEquals(result, listOf(elem, elem, elem, elem))
    }

    @Test
    fun `should return stats with -1 counter if client return null`(): Unit = runBlocking {
        val hashtag = "hashtag"
        mockClientFunc(hashtag, null)
        val res = vkManager.getHashTagStatsForNHoursFromNow(hashtag, 1)
        Assert.assertEquals(res.size, 1)
        Assert.assertEquals(res[0], Stats(hashtag, -1))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `should throw if hashtag is empty`(): Unit = runBlocking {
        val hashtag = ""
        mockClientFunc(hashtag, Response(2, 2))
        vkManager.getHashTagStatsForNHoursFromNow(hashtag, 5)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `should throw if hours param equals 0`(): Unit = runBlocking {
        val hashtag = "tag"
        mockClientFunc(hashtag, Response(2, 2))
        vkManager.getHashTagStatsForNHoursFromNow(hashtag, 0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `should throw if hours var is negative`(): Unit = runBlocking {
        val hashtag = "tag"
        mockClientFunc(hashtag, Response(2, 2))
        vkManager.getHashTagStatsForNHoursFromNow(hashtag, -1)
    }


    @Test(expected = IllegalArgumentException::class)
    fun `should throw if hours param is more than 24`(): Unit = runBlocking {
        val hashtag = "tag"
        mockClientFunc(hashtag, Response(2, 2))
        vkManager.getHashTagStatsForNHoursFromNow(hashtag, 25)
    }



    private fun mockClientFunc(hashtag: String, response: Response?) = runBlocking {
        Mockito
            .`when`(vkClient.getStatByHashtagsForHourBeforeTime(safeEq(hashtag), anyLong(), anyLong()))
            .thenReturn(response)
    }

    private fun <T : Any> safeEq(value: T): T = eq(value) ?: value
}