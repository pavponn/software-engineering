package com.github.pavponn.todo.model

/**
 * @author pavponn
 */
data class Todo(
    public var id: Int = 0,
    public var description: String = "",
    public var done: Boolean = false,
    public var listId: Int = 0
) {

}