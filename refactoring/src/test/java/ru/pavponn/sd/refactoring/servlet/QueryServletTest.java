package ru.pavponn.sd.refactoring.servlet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.pavponn.sd.refactoring.dao.ProductDaoRW;
import ru.pavponn.sd.refactoring.models.Product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.pavponn.sd.refactoring.servlet.TestUtils.mockQueryRequest;
import static ru.pavponn.sd.refactoring.servlet.TestUtils.mockResponse;

public class QueryServletTest {
    QueryServlet servlet;
    StringWriter stringWriter;
    PrintWriter writer;

    private final static String EXP_PRODUCT_NAME = "MacBook16";
    private final static long EXP_PRODUCT_PRICE = 2399;
    private final static String CHP_PRODUCT_NAME = "ThinkPad";
    private final static long CHP_PRODUCT_PRICE = 999;

    Product product1 = new Product(EXP_PRODUCT_NAME, EXP_PRODUCT_PRICE);
    Product product2 = new Product(CHP_PRODUCT_NAME, CHP_PRODUCT_PRICE);

    @Before
    public void setUp() throws SQLException {
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
    }

    @Test
    public void shouldShowProductWithMaxPrice() throws IOException {
        ProductDaoRW productDao = mock(ProductDaoRW.class);
        when(productDao.getMaxPriceProduct()).thenReturn(product1);
        servlet = new QueryServlet(productDao);
        HttpServletRequest request = mockQueryRequest("max");
        HttpServletResponse response = mockResponse(writer);
        servlet.doGet(request, response);
        String expectedResult =
                "<html><body>\n" +
                        "<h1>Product with max price: </h1>\n" +
                        EXP_PRODUCT_NAME + "\t" + EXP_PRODUCT_PRICE + "</br>\n" +
                        "</body></html>\n";

        Assert.assertEquals(expectedResult, stringWriter.toString());
    }

    @Test
    public void shouldShowProductWithMinPrice() throws IOException {
        ProductDaoRW productDao = mock(ProductDaoRW.class);
        when(productDao.getMinPriceProduct()).thenReturn(product2);
        servlet = new QueryServlet(productDao);
        HttpServletRequest request = mockQueryRequest("min");
        HttpServletResponse response = mockResponse(writer);
        servlet.doGet(request, response);

        String expectedResult = "<html><body>\n" +
                "<h1>Product with min price: </h1>\n" +
                CHP_PRODUCT_NAME + "\t" + CHP_PRODUCT_PRICE + "</br>\n" +
                "</body></html>\n";

        Assert.assertEquals(expectedResult, stringWriter.toString());
    }

    @Test
    public void shouldShowCorrectSumOfAllProducts() throws IOException {
        ProductDaoRW productDao = mock(ProductDaoRW.class);
        when(productDao.getSumPrices()).thenReturn(CHP_PRODUCT_PRICE + EXP_PRODUCT_PRICE);
        servlet = new QueryServlet(productDao);
        HttpServletRequest request = mockQueryRequest("sum");
        HttpServletResponse response = mockResponse(writer);
        servlet.doGet(request, response);

        String expectedResult = "<html><body>\n" +
                "Summary price: \n" +
                "3398\n" +
                "</body></html>\n";

        Assert.assertEquals(expectedResult, stringWriter.toString());
    }

    @Test
    public void shouldShowCorrectNumberOfProducts() throws IOException {
        ProductDaoRW productDao = mock(ProductDaoRW.class);
        when(productDao.getCount()).thenReturn(2L);
        servlet = new QueryServlet(productDao);
        HttpServletRequest request = mockQueryRequest("count");
        HttpServletResponse response = mockResponse(writer);
        servlet.doGet(request, response);

        String expectedResult = "<html><body>\n" +
                "Number of products: \n" +
                "2\n" +
                "</body></html>\n";

        Assert.assertEquals(expectedResult, stringWriter.toString());
    }

}
