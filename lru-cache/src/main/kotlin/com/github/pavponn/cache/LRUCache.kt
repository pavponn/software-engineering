package com.github.pavponn.cache

class LRUCache<K, V>(capacity: Int): AbstractLRUCache<K, V>(capacity) {
    private var size = 0

    override fun sizeImpl(): Int {
        return size
    }

    override fun getImpl(key: K): V? {
        val node = map[key] ?: return null
        bringToFront(node)
        return node.value
    }

    override fun putImpl(key: K, value: V) {
        var node = map[key]
        if (node != null) {
            node.value = value
            bringToFront(node)
            return
        }

        node = Node(key, value)
        map[key] = node
        makeHead(node)
        if (isEmpty()) {
            tail = head
        }
        if (isFull()) {
            removeFromTail()
        } else {
           size++
        }
    }

    override fun removeImpl(key: K): Boolean {
        if (!containsKey(key)) {
            return false
        }
        val node = map[key]
        map.remove(key)
        when {
            tail == head -> {
                assert(node == tail)
                tail = null
                head = null
            }
            node == head -> {
                head = node?.next
            }
            node == tail -> {
                tail = node?.prev
            }
            else -> {
                val prev = node?.prev
                val next = node?.next
                prev?.next = next
                next?.prev = prev
            }
        }
        size--
        node?.prev = null
        node?.next = null
        return true
    }

    private fun bringToFront(node: Node) {
        if (node == head) {
            return
        }
        deleteNode(node)
        makeHead(node)
    }

    private fun makeHead(node: Node) {
        if (head == null) {
            head = node
            return
        }
        node.next = head
        head!!.prev = node
        node.prev = null
        head = node
    }

    private fun removeFromTail() {
        map.remove(tail!!.key)
        val oldTail = tail!!
        tail = tail!!.prev
        oldTail.prev = null
        tail!!.next = null
    }

    private fun deleteNode(node: Node) {
        if (node == tail) {
            tail = node.prev
            node.next = null
            node.prev = null
        }
        val prev = node.prev
        val next = node.next
        if (next != null) {
            next.prev = prev
        }
        if (prev != null) {
            prev.next = next
        }
        node.next = null
        node.prev = null
    }

}