package com.github.pavponn.todo.dao

import com.github.pavponn.todo.model.Todo
import com.github.pavponn.todo.model.TodoList
import com.github.pavponn.todo.model.TodoStatus

/**
 * @author pavponn
 */
public interface TodoDao {

    fun getTodos(): List<Todo>

    fun addTodo(todo: Todo)

    fun deleteTodo(id: Int)

    fun setAsDone(id: Int)

    fun setAsTodo(id: Int)

    fun getTodoLists(): List<TodoList>

    fun addTodoList(todoList: TodoList)

    fun removeTodoList(id: Int)

}