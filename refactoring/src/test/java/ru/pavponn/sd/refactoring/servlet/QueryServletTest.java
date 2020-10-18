package ru.pavponn.sd.refactoring.servlet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.pavponn.sd.refactoring.servlet.TestUtils.addProductToDB;
import static ru.pavponn.sd.refactoring.servlet.TestUtils.mockResponse;

public class QueryServletTest {
    QueryServlet servlet;
    StringWriter stringWriter;
    PrintWriter writer;

    private final static String EXP_PRODUCT_NAME = "MacBook16";
    private final static String EXP_PRODUCT_PRICE = "2399";
    private final static String CHP_PRODUCT_NAME = "ThinkPad";
    private final static String CHP_PRODUCT_PRICE = "999";

    private final static String DB_NAME = "test_unit.db";

    @BeforeClass
    public static void setUpDatabase() throws SQLException {
        TestUtils.createProductsTable(DB_NAME);
    }

    @Before
    public void setUp() throws SQLException {
        TestUtils.clearProductsTable(DB_NAME);
        servlet = new QueryServlet(DB_NAME);
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        addProductToDB(DB_NAME, EXP_PRODUCT_NAME, EXP_PRODUCT_PRICE);
        addProductToDB(DB_NAME, CHP_PRODUCT_NAME, CHP_PRODUCT_PRICE);
    }

    @Test
    public void shouldShowProductWithMaxPrice() throws IOException {
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

        HttpServletRequest request = mockQueryRequest("count");
        HttpServletResponse response = mockResponse(writer);
        servlet.doGet(request, response);

        String expectedResult = "<html><body>\n" +
                "Number of products: \n" +
                "2\n" +
                "</body></html>\n";

        Assert.assertEquals(expectedResult, stringWriter.toString());
    }


    private static HttpServletRequest mockQueryRequest(String cmd) {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("command")).thenReturn(cmd);
        return request;
    }

}
