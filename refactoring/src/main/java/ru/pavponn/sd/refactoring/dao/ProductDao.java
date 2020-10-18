package ru.pavponn.sd.refactoring.dao;

import ru.pavponn.sd.refactoring.models.Product;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductDao implements ProductDaoReadWrite {
    private final String dbName;

    public ProductDao(String dbName) {
        this.dbName = dbName;
    }

    @Override
    public List<Product> getAllProducts() {
        try {
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:" + dbName)) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT");
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
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:" + dbName)) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1");

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
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:" + dbName)) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1");

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
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:" + dbName)) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT SUM(price) FROM PRODUCT");
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
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:" + dbName)) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM PRODUCT");
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
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:" + dbName)) {
                String sql = "INSERT INTO PRODUCT " +
                        "(NAME, PRICE) VALUES (\"" + product.getName() + "\"," + product.getPrice() + ")";
                Statement stmt = c.createStatement();
                stmt.executeUpdate(sql);
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
