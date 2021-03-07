package com.github.pavponn.model

interface Delayable {

    fun setDelays(vararg delays: Int)

    fun nextDelay(): Int

    fun getDelay(): Int
}