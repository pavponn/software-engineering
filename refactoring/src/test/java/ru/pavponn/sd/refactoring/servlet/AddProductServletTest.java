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

import static org.mockito.Mockito.*;
import static ru.pavponn.sd.refactoring.servlet.TestUtils.mockResponse;

public class AddProductServletTest {
    AddProductServlet servlet;
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
        servlet = new AddProductServlet(DB_NAME);
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
    }

    @Test
    public void shouldAddProductCorrectly() throws IOException {
        HttpServletRequest request = mockAddRequest("iphone6", "999");
        HttpServletResponse response = mockResponse(writer);
        servlet.doGet(request, response);
        verify(request, atLeast(1)).getParameter("name");
        verify(request, atLeast(1)).getParameter("price");
        writer.flush();
        Assert.assertEquals(stringWriter.toString(), "OK\n");
    }


    private static HttpServletRequest mockAddRequest(String product, String price) {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("name")).thenReturn(product);
        when(request.getParameter("price")).thenReturn(price);
        return request;
    }
}
