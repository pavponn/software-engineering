package ru.pavponn.sd.refactoring.dao;

import ru.pavponn.sd.refactoring.dbconnection.DBConnectionManager;
import ru.pavponn.sd.refactoring.models.Product;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static ru.pavponn.sd.refactoring.sql.ProductSqlCommands.*;

public class ProductDao implements ProductDaoRW {

    private final DBConnectionManager connectionManager;

    public ProductDao(DBConnectionManager connectionManager) {
       this.connectionManager = connectionManager;
    }

    @Override
    public List<Product> getAllProducts() {
        try {
            try (Connection c = connectionManager.getConnection()) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery(getAllProductsSql());
                List<Product> products = new ArrayList<>();
                while (rs.next()) {
                    String name = rs.getString("name");
                    long price = rs.getLong("price");
                    products.add(new Product(name, price));
                }

                rs.close();
                stmt.close();
                return products;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product getMinPriceProduct() {
        try {
            try (Connection c = connectionManager.getConnection()) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery(getMinPriceProductSql());

                String name = rs.getString("name");
                long price = rs.getLong("price");
                Product product = new Product(name, price);

                rs.close();
                stmt.close();
                return product;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product getMaxPriceProduct() {
        try {
            try (Connection c = connectionManager.getConnection()) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery(getMaxPriceProductSql());

                String name = rs.getString("name");
                long price = rs.getLong("price");
                Product product = new Product(name, price);

                rs.close();
                stmt.close();
                return product;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long getSumPrices() {
        try {
            try (Connection c = connectionManager.getConnection()) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery(getSumPricesSql());
                long sum = rs.getLong(1);
                rs.close();
                stmt.close();
                return sum;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long getCount() {
        try {
            try (Connection c = connectionManager.getConnection()) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery(getCountSql());
                long sum = rs.getLong(1);
                rs.close();
                stmt.close();
                return sum;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean addProduct(Product product) {
        try {
            try (Connection c = connectionManager.getConnection()) {
                Statement stmt = c.createStatement();
                stmt.executeUpdate(addProductSql(product.getName(), product.getPrice()));
                stmt.close();
            } catch (Exception e) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
