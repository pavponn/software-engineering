package com.github.pavponn.todo.dao

import com.github.pavponn.todo.model.Todo
import com.github.pavponn.todo.model.TodoList
import com.github.pavponn.todo.model.TodoStatus

/**
 * @author pavponn
 */
class TodoInMemoryDao : TodoDao {
    private val todos = mutableListOf<Todo>()
    private val todoLists = mutableListOf<TodoList>()

    override fun getTodos(): List<Todo> = todos

    override fun getTodoLists(): List<TodoList> = todoLists

    override fun addTodo(todo: Todo) {
        todos.add(todo)
    }

    override fun deleteTodo(id: Int) {
        todos.removeIf { it.id == id }
    }

    override fun setAsDone(id: Int) {
        todos.find { it.id == id }?.status = TodoStatus.DONE
    }

    override fun setAsTodo(id: Int) {
        todos.find { it.id == id }?.status = TodoStatus.TODO
    }

    override fun addTodoList(todoList: TodoList) {
        todoLists.add(todoList)
    }

    override fun removeTodoList(id: Int) {
        todoLists.removeIf { it.id == id }
    }

}