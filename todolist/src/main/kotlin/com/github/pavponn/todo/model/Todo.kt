package com.github.pavponn.todo.model

/**
 * @author pavponn
 */
data class Todo(var id: Int, var description: String, var status: TodoStatus = TodoStatus.TODO, var listId: Int?) {
}