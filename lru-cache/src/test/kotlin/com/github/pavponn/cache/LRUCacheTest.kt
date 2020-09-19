package com.github.pavponn.cache

import org.junit.*
import java.lang.IllegalArgumentException


class LRUCacheTest {
    companion object {
        const val KEY_1 = 0
        const val KEY_2 = 2
        const val KEY_3 = 3
        const val KEY_4 = 7
        const val VAL_1 = 2
        const val VAL_2 = 4
        const val VAL_3 = 7
        const val VAL_4 = 12
        const val OTHER_TEST_KEY = 1231
        const val OTHER_TEST_VAL = 2311332
        const val INIT_DEFAULT_SIZE = 4
        const val DEFAULT_CAPACITY = 7
    }
    lateinit var defaultLRUCache: LRUCache<Int, Int>

    @Before fun setUp() {
        defaultLRUCache = LRUCache(DEFAULT_CAPACITY)
        defaultLRUCache.put(KEY_1, VAL_1)
        defaultLRUCache.put(KEY_2, VAL_2)
        defaultLRUCache.put(KEY_3, VAL_3)
        defaultLRUCache.put(KEY_4, VAL_4)
    }

    @Test
    fun `should construct valid empty LRUCache`() {
        val lruCache = LRUCache<Int, Int>(1)
        Assert.assertFalse(lruCache.isFull())
        Assert.assertTrue(lruCache.isEmpty())
        Assert.assertEquals(0, lruCache.size())
    }

    @Test(expected = IllegalArgumentException::class)
    fun `should throw exception for zero capacity`() {
        val lruCache = LRUCache<Int, Int>(0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `should throw exception for negative capacity`() {
        val lruCache = LRUCache<Int, Int>(-2)
    }

    @Test
    fun `should return valid capacity`() {
        Assert.assertEquals(DEFAULT_CAPACITY, defaultLRUCache.capacity())
    }

    @Test
    fun `should return valid size`() {
        Assert.assertEquals(INIT_DEFAULT_SIZE, defaultLRUCache.size())
    }

    @Test
    fun `should not be full if size is less than capacity`() {
        Assert.assertFalse(defaultLRUCache.isFull())
    }

    @Test
    fun `should not be empty if some elements are stored`() {
        Assert.assertFalse(defaultLRUCache.isEmpty())
    }

    @Test
    fun `should get right value from LRUCache`() {
        Assert.assertEquals(VAL_1, defaultLRUCache.get(KEY_1))
        Assert.assertEquals(VAL_2, defaultLRUCache.get(KEY_2))
        Assert.assertEquals(VAL_3, defaultLRUCache.get(KEY_3))
        Assert.assertEquals(VAL_4, defaultLRUCache.get(KEY_4))
    }

    @Test
    fun `should return null for non existing key`() {
        Assert.assertNull(defaultLRUCache.get(OTHER_TEST_KEY))
    }

    @Test
    fun `should put new value into LRUCache correctly`() {
        defaultLRUCache.put(OTHER_TEST_KEY, OTHER_TEST_VAL)
        Assert.assertEquals(OTHER_TEST_VAL, defaultLRUCache.get(OTHER_TEST_KEY))
        Assert.assertEquals(INIT_DEFAULT_SIZE + 1, defaultLRUCache.size())
    }

    @Test
    fun `should update value for key if key is alreadt stored in LRUCache`() {
        defaultLRUCache.put(KEY_2, OTHER_TEST_VAL)
        Assert.assertEquals(OTHER_TEST_VAL, defaultLRUCache.get(KEY_2))
        Assert.assertEquals(INIT_DEFAULT_SIZE, defaultLRUCache.size())
    }

    @Test
    fun `should increment size correctly`() {
        val lruCache = LRUCache<Int, Int>(5)
        lruCache.put(0, 1)
        Assert.assertEquals(1, lruCache.size())
        lruCache.put(1, 1)
        Assert.assertEquals(2, lruCache.size())
        lruCache.put(2, 1)
        Assert.assertEquals(3, lruCache.size())
        lruCache.put(3, 1)
        Assert.assertEquals(4, lruCache.size())
        lruCache.put(3, 2)
        Assert.assertEquals(4, lruCache.size())
        lruCache.put(5, 100)
        Assert.assertEquals(5, lruCache.size())
        lruCache.put(100, 20)
        Assert.assertEquals(5, lruCache.size())
    }

    @Test
    fun `should replace least recently used element when LRUCache is full #1`() {
        val lruCache = LRUCache<Int, Int>(3)
        lruCache.put(1, 2)
        lruCache.put(2, 3)
        lruCache.put(3, 4)
        lruCache.put(4, 5)
        Assert.assertFalse(lruCache.containsKey(1))
        Assert.assertTrue(lruCache.containsAllKeys(2, 3, 4))
    }

    @Test
    fun `should replace least recently used element when LRUCache is full #2`() {
        val lruCache = LRUCache<Int, Int>(4)
        lruCache.put(1, 2)
        lruCache.put(2, 3)
        lruCache.put(3, 4)
        lruCache.put(4, 5)
        lruCache.get(1)
        lruCache.get(2)
        lruCache.put(5, 5)
        Assert.assertFalse(lruCache.containsKey(3))
        Assert.assertTrue(lruCache.containsAllKeys(1, 2, 4, 5))
    }
}