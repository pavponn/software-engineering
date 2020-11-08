package com.github.pavponn.todo.config

import com.github.pavponn.todo.dao.TodoDao
import com.github.pavponn.todo.dao.TodoInMemoryDao
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author pavponn
 */
open class InMemoryDaoContextConfiguration {
    @Bean
    open fun todoDao(): TodoDao {
        return TodoInMemoryDao()
    }
}