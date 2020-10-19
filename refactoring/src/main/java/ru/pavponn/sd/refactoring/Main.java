package ru.pavponn.sd.refactoring;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.pavponn.sd.refactoring.dao.ProductDao;
import ru.pavponn.sd.refactoring.dao.ProductDaoRW;
import ru.pavponn.sd.refactoring.dbconnection.DBConnectionManager;
import ru.pavponn.sd.refactoring.dbconnection.DBConnectionManagerImpl;
import ru.pavponn.sd.refactoring.servlet.AddProductServlet;
import ru.pavponn.sd.refactoring.servlet.GetProductsServlet;
import ru.pavponn.sd.refactoring.servlet.QueryServlet;

import java.sql.Connection;
import java.sql.Statement;

/**
 * @author akirakozov
 */
public class Main {
    public static void main(String[] args) throws Exception {
        DBConnectionManager connectionManager = new DBConnectionManagerImpl("jdbc:sqlite:test.db");
        try (Connection c = connectionManager.getConnection()) {
            String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        }

        Server server = new Server(8081);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        ProductDaoRW productDao = new ProductDao(connectionManager);
        context.addServlet(new ServletHolder(new AddProductServlet(productDao)), "/add-product");
        context.addServlet(new ServletHolder(new GetProductsServlet(productDao)),"/get-products");
        context.addServlet(new ServletHolder(new QueryServlet(productDao)),"/query");

        server.start();
        server.join();
    }
}
