package com.github.pavponn.todo.dao

import com.github.pavponn.todo.model.Todo
import com.github.pavponn.todo.model.TodoList
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.support.JdbcDaoSupport
import javax.sql.DataSource

/**
 * @author pavponn
 */
class TodoJdbcDao(dataSource: DataSource) : JdbcDaoSupport(), TodoDao {
    companion object {
        private const val SQL_TODO_ALL = "select * from Todos"
        private const val SQL_TODO_BY_LIST_ID = "select * from Todos where listId = ?"
        private const val SQL_TODO_DELETE = "delete from Todos where id = ?"
        private const val SQL_TODO_DELETE_BY_LIST = "delete from Todos where listId = ?"
        private const val SQL_TODO_INSERT = "insert into Todos(description, done, listid) values(?,?,?)"
        private const val SQL_TODO_UPDATE_DONE = "update todos set done = ? where id = ?"
        private const val SQL_TODOLIST_ALL = "select * from TodoLists"
        private const val SQL_TODOLIST_BY_ID = "select * from TodoLists where id = ?"
        private const val SQL_TODOLIST_INSERT = "insert into TodoLists(name) values(?)"
        private const val SQL_TODOLIST_DELETE = "delete from Todolists where id = ?"
    }

    init {
        setDataSource(dataSource)
    }

    override fun getTodos(): List<Todo> {
        return getTodosByRequest(SQL_TODO_ALL, emptyArray())
    }

    override fun getTodos(listId: Int): List<Todo> {
        return getTodosByRequest(SQL_TODO_BY_LIST_ID, arrayOf(listId))
    }

    override fun addTodo(description: String, listId: Int) {
        jdbcTemplate?.update(SQL_TODO_INSERT, description, false, listId)
    }

    override fun deleteTodo(id: Int) {
        jdbcTemplate?.update(SQL_TODO_DELETE, id)
    }

    override fun deleteTodos(listId: Int) {
        jdbcTemplate?.update(SQL_TODO_DELETE_BY_LIST, listId)
    }

    override fun updateDone(id: Int, done: Boolean) {
        jdbcTemplate?.update(SQL_TODO_UPDATE_DONE, done, id)
    }

    override fun getTodoLists(): List<TodoList> {
        return getTodoListsByRequest(SQL_TODOLIST_ALL, emptyArray())
    }

    override fun getTodoList(id: Int): TodoList {
        return getTodoListsByRequest(SQL_TODOLIST_BY_ID, arrayOf(id))[0]
    }

    override fun addTodoList(name: String) {
        jdbcTemplate?.update(SQL_TODOLIST_INSERT, name)
    }

    override fun deleteTodoList(id: Int) {
        jdbcTemplate?.update(SQL_TODOLIST_DELETE, id)
    }

    private fun getTodoListsByRequest(sql: String, params: Array<Any>): List<TodoList> {
        return jdbcTemplate?.query(sql, params, BeanPropertyRowMapper(TodoList::class.java)) ?: emptyList()
    }

    private fun getTodosByRequest(sql: String, params: Array<Any>): List<Todo> {
        return jdbcTemplate?.query(sql, params, BeanPropertyRowMapper(Todo::class.java)) ?: emptyList()
    }
}