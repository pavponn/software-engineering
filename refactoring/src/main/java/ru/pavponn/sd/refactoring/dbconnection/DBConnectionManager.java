package ru.pavponn.sd.refactoring.dbconnection;

import java.sql.Connection;
import java.sql.SQLException;

public interface DBConnectionManager {

    Connection getConnection() throws SQLException;
}
