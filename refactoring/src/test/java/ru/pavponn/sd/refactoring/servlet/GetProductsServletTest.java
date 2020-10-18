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
import static ru.pavponn.sd.refactoring.servlet.TestUtils.addProductToDB;
import static ru.pavponn.sd.refactoring.servlet.TestUtils.mockResponse;

public class GetProductsServletTest {
    GetProductsServlet servlet;
    StringWriter stringWriter;
    PrintWriter writer;

    private final static String DB_NAME = "test_unit.db";

    @BeforeClass
    public static void setUpDatabase() throws SQLException {
        TestUtils.createProductsTable(DB_NAME);
    }

    @Before
    public void setUp() throws SQLException {
        TestUtils.clearProductsTable(DB_NAME);
        servlet = new GetProductsServlet(DB_NAME);
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
    }

    @Test
    public void shouldShowNoProductsIfEmpty() throws IOException {
        HttpServletRequest request = mockGetProductRequest();
        HttpServletResponse response = mockResponse(writer);
        servlet.doGet(request, response);
        String expectedResult = "<html><body>\n" + "</body></html>\n";
        Assert.assertEquals(expectedResult, stringWriter.toString());
    }

    @Test
    public void shouldShowAddedProduct() throws SQLException, IOException {
        String name = "bag";
        String price = "200";
        addProductToDB(DB_NAME, name, price);
        HttpServletRequest request = mockGetProductRequest();
        HttpServletResponse response = mockResponse(writer);
        servlet.doGet(request, response);
        String expectedResult = "<html><body>\n" +
                name + "\t" + price + "</br>\n" +
                "</body></html>\n";
        Assert.assertEquals(expectedResult, stringWriter.toString());
    }

    @Test
    public void shouldShowMultipleAddedProducts() throws SQLException, IOException {
        String name1 = "bag";
        String price1 = "200";
        String name2 = "iphone12";
        String price2 = "1399";
        addProductToDB(DB_NAME, name1, price1);
        addProductToDB(DB_NAME, name2, price2);
        HttpServletRequest request = mockGetProductRequest();
        HttpServletResponse response = mockResponse(writer);
        servlet.doGet(request, response);
        String expectedResult =
                "<html><body>\n" +
                        name1 + "\t" + price1 + "</br>\n" +
                        name2 + "\t" + price2 + "</br>\n" +
                        "</body></html>\n";
        Assert.assertEquals(expectedResult, stringWriter.toString());
    }

    private static HttpServletRequest mockGetProductRequest() {
        return mock(HttpServletRequest.class);
    }
}
