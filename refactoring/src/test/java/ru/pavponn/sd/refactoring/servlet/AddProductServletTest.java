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

import static org.mockito.Mockito.*;
import static ru.pavponn.sd.refactoring.servlet.TestUtils.mockAddRequest;
import static ru.pavponn.sd.refactoring.servlet.TestUtils.mockResponse;

public class AddProductServletTest {
    AddProductServlet servlet;
    StringWriter stringWriter;
    PrintWriter writer;

    @Before
    public void setUp() {
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
    }

    @Test
    public void shouldAddProductCorrectly() throws IOException {
        String name = "iphone6";
        String price = "999";
        Product product = new Product(name, Long.parseLong(price));

        HttpServletRequest request = mockAddRequest(name, price);
        HttpServletResponse response = mockResponse(writer);
        ProductDaoRW productDao = mock(ProductDaoRW.class);
        doReturn(true).when(productDao).addProduct(product);

        servlet = new AddProductServlet(productDao);
        servlet.doGet(request, response);
        verify(request, atLeast(1)).getParameter("name");
        verify(request, atLeast(1)).getParameter("price");
        verify(productDao, times(1)).addProduct(product);
        writer.flush();
        Assert.assertEquals(stringWriter.toString(), "OK\n");
    }

}
