package ru.pavponn.sd.refactoring.dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionManagerImpl implements DBConnectionManager {
    private final String name;

    public DBConnectionManagerImpl(String name) {
        this.name = name;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(name);
    }
}
