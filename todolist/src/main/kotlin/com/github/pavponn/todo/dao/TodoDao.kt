package com.github.pavponn.todo.dao

import com.github.pavponn.todo.model.Todo
import com.github.pavponn.todo.model.TodoList

/**
 * @author pavponn
 */
public interface TodoDao {

    fun getTodos(): List<Todo>

    fun getTodos(listId: Int): List<Todo>

    fun addTodo(description: String, listId: Int)

    fun deleteTodo(id: Int)

    fun deleteTodos(listId: Int)

    fun updateDone(id: Int, done: Boolean)

    fun getTodoLists(): List<TodoList>

    fun getTodoList(id: Int): TodoList

    fun addTodoList(name: String)

    fun deleteTodoList(id: Int)

}