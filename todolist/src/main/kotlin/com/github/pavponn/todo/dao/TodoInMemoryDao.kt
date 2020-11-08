package com.github.pavponn.todo.dao

import com.github.pavponn.todo.model.Todo
import com.github.pavponn.todo.model.TodoList

/**
 * @author pavponn
 */
class TodoInMemoryDao : TodoDao {
    private val todos = mutableListOf<Todo>()
    private val todoLists = mutableListOf<TodoList>()

    override fun getTodos(): List<Todo> = todos

    override fun getTodos(listId: Int): List<Todo> = todos.filter { it.listId == listId }

    override fun getTodoLists(): List<TodoList> = todoLists

    override fun addTodo(description: String, listId: Int) {
        val id = (todos.maxByOrNull { it.id }?.id ?: 0) + 1
        val todo = Todo(id, description, false, listId)
        todos.add(todo)
    }

    override fun deleteTodo(id: Int) {
        todos.removeIf { it.id == id }
    }

    override fun deleteTodos(listId: Int) {
        todos.removeIf { it.listId == listId }
    }

    override fun setAsDone(id: Int) {
        todos.find { it.id == id }?.isDone = true
    }

    override fun setAsTodo(id: Int) {
        todos.find { it.id == id }?.isDone = false
    }

    override fun addTodoList(name: String) {
        val id = (todoLists.maxByOrNull { it.id }?.id ?: 0) + 1
        val todoList = TodoList(id, name)
        todoLists.add(todoList)
    }

    override fun deleteTodoList(id: Int) {
        todoLists.removeIf { it.id == id }
    }

}