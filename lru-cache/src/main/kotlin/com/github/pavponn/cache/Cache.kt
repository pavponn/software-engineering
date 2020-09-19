package com.github.pavponn.cache

interface Cache<K, V> {
    fun put(key: K, value: V)

    fun get(key: K): V?

    fun containsKey(key: K): Boolean

    fun containsAllKeys(vararg keys: K): Boolean

    fun size(): Int

    fun capacity(): Int

    fun isEmpty(): Boolean

    fun isFull(): Boolean
}