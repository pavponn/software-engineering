package ru.pavponn.sd.refactoring.response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.pavponn.sd.refactoring.models.Product;

import java.util.Arrays;

public class ResponseHtmlBuilderTest {
    private ResponseHtmlBuilder builder;

    @Before
    public void setUp() {
        builder = new ResponseHtmlBuilder();
    }

    @Test
    public void shouldBuildHtmlWithNoContent() {
        String response = builder.build();
        String expected = "<html><body>\n" + "</body></html>";
        Assert.assertEquals(expected, response);
    }

    @Test
    public void shouldBuildHtmlWithOneLine() {
        final String line = "This is line";
        String response = builder
                .addLine(line)
                .build();
        String expected = "<html><body>\n" +
                line + "\n" +
                "</body></html>";
        Assert.assertEquals(expected, response);
    }

    @Test
    public void shouldBuildHtmlWithHeader() {
        final String header = "<h1>HEADER</h1>";
        final String line = "This is line";
        builder = new ResponseHtmlBuilder(header);
        String response = builder
                .addLine(line)
                .build();
        String expected = "<html><body>\n" +
                header + "\n" +
                line + "\n" +
                "</body></html>";

        Assert.assertEquals(expected, response);
    }
    @Test
    public void shouldBuildHtmlWithSeveralLines() {
        final String line1 = "This is line1";
        final String line2 = "This is line2";
        final String line3 = "This is line3";
        String response = builder
                .addLine(line1)
                .addLine(line2)
                .addLine(line3)
                .build();
        String expected = "<html><body>\n" +
                line1 + "\n" +
                line2 + "\n" +
                line3 + "\n" +
                "</body></html>";
        Assert.assertEquals(expected, response);
    }

    @Test
    public void shouldBuildHtmlWithOneProduct() {
        final Product product = new Product("product", 200);
        String response = builder
                .addProduct(product)
                .build();
        String expected = "<html><body>\n" +
                product.getName() + "\t" + product.getPrice() + "</br>\n" +
                "</body></html>";
        Assert.assertEquals(expected, response);
    }

    @Test
    public void shouldBuildHtmlWithSeveralProducts() {
        final Product product1 = new Product("product1", 200);
        final Product product2 = new Product("product2", 300);
        final Product product3 = new Product("product3", 400);
        String response = builder
                .addProduct(product1)
                .addProduct(product2)
                .addProduct(product3)
                .build();
        String expected = "<html><body>\n" +
                product1.getName() + "\t" + product1.getPrice() + "</br>\n" +
                product2.getName() + "\t" + product2.getPrice() + "</br>\n" +
                product3.getName() + "\t" + product3.getPrice() + "</br>\n" +
                "</body></html>";
        Assert.assertEquals(expected, response);
    }

    @Test
    public void shouldBuildHtmlWithSeveralProductsList() {
        final Product product1 = new Product("product1", 200);
        final Product product2 = new Product("product2", 300);
        final Product product3 = new Product("product3", 400);
        String response = builder
                .addProducts(Arrays.asList(product1, product2, product3))
                .build();
        String expected = "<html><body>\n" +
                product1.getName() + "\t" + product1.getPrice() + "</br>\n" +
                product2.getName() + "\t" + product2.getPrice() + "</br>\n" +
                product3.getName() + "\t" + product3.getPrice() + "</br>\n" +
                "</body></html>";
        Assert.assertEquals(expected, response);
    }

}
