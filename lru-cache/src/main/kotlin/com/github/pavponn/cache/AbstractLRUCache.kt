package com.github.pavponn.cache

abstract class AbstractLRUCache<K, V>(capacity: Int): Cache<K, V> {
    protected val cacheCapacity = capacity
    protected var head: Node? = null
    protected var tail: Node? = null
    protected val map = hashMapOf<K, Node>()

    inner class Node(val key: K, var value: V) {
        var prev: Node? = null
        var next: Node? = null
    }

    init {
        if (capacity <= 0) {
            throw IllegalArgumentException("Cache capacity should be positive")
        }
    }

    final override fun get(key: K): V? {
        checkHeadAndTailPut()
        val beforeSize = size()
        val valueBefore = map[key]?.value
        val getResult = getImpl(key)
        val afterSize = size()
        assert(beforeSize == afterSize) { "'get' method should not mutate state" }
        assert(map[key]?.value == getResult) { "Invalid result in 'get' method ${getResult} ${map[key]}" }
        assert(valueBefore == map[key]?.value) { "'get' method should not mutate values" }
        checkHeadAndTailPut()
        return getResult
    }

    final override fun containsKey(key: K): Boolean {
        return map.containsKey(key)
    }

    final override fun containsAllKeys(vararg keys: K): Boolean {
        for (key in keys) when {
            !containsKey(key) -> return false
        }
        return true
    }

    final override fun put(key: K, value: V) {
        checkHeadAndTailPut()
        val beforeSize = size()
        val isFullBefore = isFull()
        val containsThisKeyBefore= map.containsKey(key)
        putImpl(key, value)
        val afterSize = size()
        val isFullAfter = isFull()
        val containsThisKeyAfter= map.containsKey(key)
        assert(
            containsThisKeyBefore && beforeSize == afterSize && isFullBefore == isFullAfter
                || !containsThisKeyBefore && !isFullBefore && beforeSize + 1 == afterSize
                || !containsThisKeyBefore && isFullBefore && beforeSize == afterSize
        ) { "Size invariant is broken in 'put' method" }
        assert(!isFullBefore || isFullBefore && isFullAfter) { "Size invariant is broken in 'put' method" }
        assert(containsThisKeyAfter) { "'Put' method doesn't add (key, value) to cache" }
        assert(map[key]!!.value!! == value) { "Invalid value after performing 'put' operation" }
        checkHeadAndTailPut()
    }

    final override fun remove(key: K): Boolean {
        checkHeadAndTailPut()
        val beforeSize = size()
        val isFullBefore = isFull()
        val containsThisKeyBefore= map.containsKey(key)
        val removeRes = removeImpl(key)
        val afterSize = size()
        val isFullAfter = isFull()
        assert(containsThisKeyBefore == removeRes)
        assert(removeRes && beforeSize == afterSize + 1 || !removeRes && beforeSize == afterSize)
        assert(
                removeRes && (isFullBefore && !isFullAfter || !isFullBefore && !isFullAfter)
                || !removeRes && isFullBefore == isFullAfter
        )
        checkHeadAndTailPut()
        return removeRes
    }

    final override fun size(): Int {
        assert(true)
        val cacheSize = sizeImpl()
        assert(cacheSize <= cacheCapacity)
        assert(cacheSize >= 0)
        return cacheSize
    }

    final override fun capacity(): Int {
        return cacheCapacity
    }

    final override fun isEmpty() = size() == 0

    final override fun isFull() = size() == cacheCapacity

    private fun checkHeadAndTailPut() {
        assert(
            (size() > 0 && head != null && tail != null)
                    || (size() == 0 && head == null && tail == null)
        )
    }

    protected abstract fun sizeImpl(): Int

    protected abstract fun getImpl(key: K): V?

    protected abstract fun putImpl(key: K, value: V)

    protected abstract fun removeImpl(key: K): Boolean
}