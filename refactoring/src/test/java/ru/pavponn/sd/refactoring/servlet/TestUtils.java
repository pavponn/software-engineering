package ru.pavponn.sd.refactoring.servlet;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestUtils {
    public static void createProductsTable(String dbName) throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:" + dbName)) {
            String createTable = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)";
            Statement stmt = c.createStatement();
            stmt.executeUpdate(createTable);
            stmt.close();
        }
    }

    public static void clearProductsTable(String dbName) throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:" + dbName)) {
            String sql = "DELETE from PRODUCT";
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

    public static HttpServletResponse mockResponse(PrintWriter writer) throws IOException {
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(writer);
        return response;
    }

    public static void addProductToDB(String dbName, String name, String price) throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:" + dbName)) {
            String sql = "INSERT INTO PRODUCT " +
                    "(NAME, PRICE) VALUES (\"" + name + "\"," + price + ")";
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

}
