package ru.pavponn.sd.refactoring.dao;

import ru.pavponn.sd.refactoring.dbconnection.DBConnectionManager;
import ru.pavponn.sd.refactoring.function.CheckedSQLFunction;
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
        CheckedSQLFunction<Statement, List<Product>> callback = stmt -> {
            ResultSet rs = stmt.executeQuery(getAllProductsSql());
            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                String name = rs.getString("name");
                long price = rs.getLong("price");
                products.add(new Product(name, price));
            }
            rs.close();
            return products;
        };
        return new Invoker<List<Product>>().perform(callback);
    }

    @Override
    public Product getMinPriceProduct() {
        CheckedSQLFunction<Statement, Product> callback = stmt -> {
            ResultSet rs = stmt.executeQuery(getMinPriceProductSql());
            String name = rs.getString("name");
            long price = rs.getLong("price");
            Product product = new Product(name, price);
            rs.close();
            return product;
        };
        return new Invoker<Product>().perform(callback);
    }

    @Override
    public Product getMaxPriceProduct() {
        CheckedSQLFunction<Statement, Product> callback = stmt -> {
            ResultSet rs = stmt.executeQuery(getMaxPriceProductSql());
            String name = rs.getString("name");
            long price = rs.getLong("price");
            Product product = new Product(name, price);
            rs.close();
            return product;
        };
        return new Invoker<Product>().perform(callback);
    }

    @Override
    public long getSumPrices() {
        CheckedSQLFunction<Statement, Long> callback = stmt -> {
            ResultSet rs = stmt.executeQuery(getSumPricesSql());
            long sum = rs.getLong(1);
            stmt.close();
            return sum;
        };
        return new Invoker<Long>().perform(callback);
    }

    @Override
    public long getCount() {
        CheckedSQLFunction<Statement, Long> callback = stmt -> {
            ResultSet rs = stmt.executeQuery(getCountSql());
            long count = rs.getLong(1);
            rs.close();
            return count;
        };
        return new Invoker<Long>().perform(callback);
    }

    @Override
    public boolean addProduct(Product product) {
        CheckedSQLFunction<Statement, Boolean> callback = stmt -> {
            stmt.executeUpdate(addProductSql(product.getName(), product.getPrice()));
            return true;
        };
        return new Invoker<Boolean>().perform(callback);

    }

    private class Invoker<R> {
        public R perform(CheckedSQLFunction<Statement, R> func) {
            try {
                try (Connection c = connectionManager.getConnection()) {
                    Statement stmt = c.createStatement();
                    R res = func.apply(stmt);
                    stmt.close();
                    return res;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
