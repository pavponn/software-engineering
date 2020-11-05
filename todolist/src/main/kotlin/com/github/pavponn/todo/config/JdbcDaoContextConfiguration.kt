package com.github.pavponn.todo.config

import com.github.pavponn.todo.dao.TodoJdbcDao
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource

/**
 * @author pavponn
 */
open class JdbcDaoContextConfiguration {

    @Bean
    fun todoJDbcDao(dataSource: DataSource): TodoJdbcDao {
        return TodoJdbcDao(dataSource)
    }

    @Bean
    fun dataSource(): DataSource {
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName("org.sqlite.JDBC")
        dataSource.url = "jdbc:sqlite:todo.db"
        dataSource.username = ""
        dataSource.password = ""
        return dataSource
    }


}