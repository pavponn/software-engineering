package com.github.pavponn.todo.config

import com.github.pavponn.todo.dao.TodoJdbcDao
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource

/**
 * @author pavponn
 */
@Configuration
open class JdbcDaoContextConfiguration {

    @Bean
    open fun todoDao(dataSource: DataSource): TodoJdbcDao {
        return TodoJdbcDao(dataSource)
    }

    @Bean
    open fun dataSource(): DataSource {
        return DriverManagerDataSource().apply {
            setDriverClassName("org.sqlite.JDBC")
            url = "jdbc:sqlite:todo.db"
            username = ""
            password = ""
        }
    }


}