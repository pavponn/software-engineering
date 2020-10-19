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
import java.util.ArrayList;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.pavponn.sd.refactoring.servlet.TestUtils.mockGetProductRequest;
import static ru.pavponn.sd.refactoring.servlet.TestUtils.mockResponse;

public class GetProductsServletTest {
    GetProductsServlet servlet;
    StringWriter stringWriter;
    PrintWriter writer;

    @Before
    public void setUp() {
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
    }

    @Test
    public void shouldShowNoProductsIfEmpty() throws IOException {
        ProductDaoRW productDao = mock(ProductDaoRW.class);
        when(productDao.getAllProducts()).thenReturn(new ArrayList<>());
        servlet = new GetProductsServlet(productDao);
        HttpServletRequest request = mockGetProductRequest();
        HttpServletResponse response = mockResponse(writer);
        servlet.doGet(request, response);
        String expectedResult = "<html><body>\n" + "</body></html>\n";
        Assert.assertEquals(expectedResult, stringWriter.toString());
    }

    @Test
    public void shouldShowAddedProduct() throws IOException {
        Product product = new Product("bag", 200);
        ProductDaoRW productDao = mock(ProductDaoRW.class);
        when(productDao.getAllProducts()).thenReturn(singletonList(product));
        servlet = new GetProductsServlet(productDao);
        HttpServletRequest request = mockGetProductRequest();
        HttpServletResponse response = mockResponse(writer);

        servlet.doGet(request, response);
        String expectedResult = "<html><body>\n" +
                product.getName() + "\t" + product.getPrice() + "</br>\n" +
                "</body></html>\n";
        Assert.assertEquals(expectedResult, stringWriter.toString());
    }

    @Test
    public void shouldShowMultipleAddedProducts() throws IOException {
        Product product1 = new Product("bag", 200);
        Product product2 = new Product("iphone12", 1399);
        ProductDaoRW productDao = mock(ProductDaoRW.class);
        when(productDao.getAllProducts()).thenReturn(asList(product1, product2));
        servlet = new GetProductsServlet(productDao);
        HttpServletRequest request = mockGetProductRequest();
        HttpServletResponse response = mockResponse(writer);

        servlet.doGet(request, response);
        String expectedResult =
                "<html><body>\n" +
                        product1.getName() + "\t" + product1.getPrice() + "</br>\n" +
                        product2.getName() + "\t" + product2.getPrice() + "</br>\n" +
                        "</body></html>\n";
        Assert.assertEquals(expectedResult, stringWriter.toString());
    }

}
