package com.github.pavponn.model

import java.util.*

class DelayableObject : Delayable {

    private val delaysQueue: Queue<Int> = ArrayDeque<Int>()

    override fun nextDelay(): Int {
        return if (delaysQueue.isNotEmpty()) {
            delaysQueue.poll()
        } else {
            0
        }
    }

    override fun getDelay(): Int {
        return if (delaysQueue.isNotEmpty()) {
            delaysQueue.peek()
        } else {
            0
        }
    }

    override fun setDelays(vararg delays: Int) {
        delaysQueue.addAll(delays.asList())
    }
}