package ru.pavponn.sd.refactoring.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.pavponn.sd.refactoring.dbconnection.DBConnectionManager;
import ru.pavponn.sd.refactoring.models.Product;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.pavponn.sd.refactoring.sql.ProductSqlCommands.*;

public class ProductDaoTest {
    private static DBConnectionManager connectionManager;
    private static Statement statement;
    private static ResultSet rs;

    @Before
    public void setUp() throws SQLException {
        connectionManager = mock(DBConnectionManager.class);
        Connection connection = mock(Connection.class);
        statement = mock(Statement.class);
        rs = mock(ResultSet.class);
        when(connectionManager.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
    }

    @Test
    public void shouldReturnAllProducts() throws SQLException {
        when(statement.executeQuery(getAllProductsSql())).thenReturn(rs);
        when(rs.next())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);
        when(rs.getString("name"))
                .thenReturn("product1")
                .thenReturn("product2");
        when(rs.getLong("price"))
                .thenReturn(300L)
                .thenReturn(245L);
        ProductDaoRW productDao = new ProductDao(connectionManager);
        List<Product> res = productDao.getAllProducts();
        Assert.assertEquals(res,
                Arrays.asList(new Product("product1", 300), new Product("product2", 245))
        );
    }

    @Test
    public void shouldAddProduct() throws SQLException {
        Product product = new Product("product", 499);
        when(statement.executeUpdate(addProductSql(product.getName(), product.getPrice()))).thenReturn(0);
        ProductDaoRW productDao = new ProductDao(connectionManager);
        Assert.assertTrue(productDao.addProduct(product));
    }

    @Test
    public void shouldReturnNumberOfProducts() throws SQLException {
        when(statement.executeQuery(getCountSql())).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getLong(1)).thenReturn(7L);
        ProductDaoRW productDao = new ProductDao(connectionManager);
        Assert.assertEquals(7L, productDao.getCount());
    }

    @Test
    public void shouldReturnSumOfProductPrices() throws SQLException {
        when(statement.executeQuery(getSumPricesSql())).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getLong(1)).thenReturn(999999L);
        ProductDaoRW productDao = new ProductDao(connectionManager);
        Assert.assertEquals(999999L, productDao.getSumPrices());
    }

    @Test
    public void shouldReturnProductWithMaxPrice() throws SQLException {
        when(statement.executeQuery(getMaxPriceProductSql())).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getString("name")).thenReturn("product1");
        when(rs.getLong("price")).thenReturn(3000L);
        ProductDaoRW productDao = new ProductDao(connectionManager);
        Assert.assertEquals(new Product("product1", 3000L), productDao.getMaxPriceProduct());
    }

    @Test
    public void shouldReturnProductWithMinPrice() throws SQLException {
        when(statement.executeQuery(getMinPriceProductSql())).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getString("name")).thenReturn("product1");
        when(rs.getLong("price")).thenReturn(99L);
        ProductDaoRW productDao = new ProductDao(connectionManager);
        Assert.assertEquals(new Product("product1", 99L), productDao.getMinPriceProduct());
    }
}
