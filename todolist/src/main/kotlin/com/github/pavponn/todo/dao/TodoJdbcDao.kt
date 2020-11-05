package com.github.pavponn.todo.dao

import com.github.pavponn.todo.model.Todo
import com.github.pavponn.todo.model.TodoList
import com.github.pavponn.todo.model.TodoStatus
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.support.JdbcDaoSupport
import javax.sql.DataSource

/**
 * @author pavponn
 */
class TodoJdbcDao(dataSource: DataSource) : JdbcDaoSupport(), TodoDao {
    companion object {
        private const val SQL_TODO_ALL = "select * from Todos"
        private const val SQL_TODO_DELETE = "delete from Todos where id = ?"
        private const val SQL_TODO_INSERT = "insert into Todos(id, description, status, listid) values(?,?,?,?)"
        private const val SQL_TODO_UPDATE_STATUS = "update todos set status = ? where id = ?"
        private const val SQL_TODOLIST_ALL = "select * from TodoLists"
        private const val SQL_TODOLIST_INSERT = "insert into TodoLists(id,name) values(?,?)"
        private const val SQL_TODOLIST_DELETE = "delete from Todolists where id = ?"
    }

    init {
        setDataSource(dataSource)
    }

    override fun getTodos(): List<Todo> {
        return getTodosByRequest(SQL_TODO_ALL)
    }

    override fun addTodo(todo: Todo) {
        jdbcTemplate?.update(SQL_TODO_INSERT, todo.id, todo.description, todo.status, todo.listId)
    }

    override fun deleteTodo(id: Int) {
        jdbcTemplate?.update(SQL_TODO_DELETE, id)
    }

    override fun setAsDone(id: Int) {
        jdbcTemplate?.update(SQL_TODO_UPDATE_STATUS, TodoStatus.DONE, id)
    }

    override fun setAsTodo(id: Int) {
        jdbcTemplate?.update(SQL_TODO_UPDATE_STATUS, TodoStatus.TODO, id)
    }

    override fun getTodoLists(): List<TodoList> {
        return getTodoListsByRequest(SQL_TODOLIST_ALL)
    }

    override fun addTodoList(todoList: TodoList) {
        jdbcTemplate?.update(SQL_TODOLIST_INSERT, todoList.id, todoList.name)
    }

    override fun removeTodoList(id: Int) {
        jdbcTemplate?.update(SQL_TODOLIST_DELETE, id)
    }

    private fun getTodoListsByRequest(sql: String): List<TodoList> {
        return jdbcTemplate?.query(sql, BeanPropertyRowMapper(TodoList::class.java)) ?: emptyList()
    }

    private fun getTodosByRequest(sql: String): List<Todo> {
        return jdbcTemplate?.query(sql, BeanPropertyRowMapper(Todo::class.java)) ?: emptyList()
    }
}