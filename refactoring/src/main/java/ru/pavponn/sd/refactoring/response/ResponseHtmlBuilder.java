package ru.pavponn.sd.refactoring.response;

import ru.pavponn.sd.refactoring.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ResponseHtmlBuilder {
    private final String header;
    private final List<String> lines = new ArrayList<>();

    private final static String OPEN = "<html><body>";
    private final static String CLOSE = "</body></html>";

    public ResponseHtmlBuilder(String header) {
        this.header = header;
    }

    public ResponseHtmlBuilder() {
        this("");
    }

    public static ResponseHtmlBuilder responseBuilder(String header) {
        return new ResponseHtmlBuilder(header);
    }

    public static ResponseHtmlBuilder responseBuilder() {
        return new ResponseHtmlBuilder();
    }

    public ResponseHtmlBuilder addLine(long l) {
        return addLine(Long.toString(l));
    }

    public ResponseHtmlBuilder addLine(String line) {
        if (line != null) {
            lines.add(line);
        }
        return this;
    }

    public ResponseHtmlBuilder addProduct(Product product) {
        if (product != null) {
            addLine(product.getName() + "\t" + product.getPrice() + "</br>");
        }

        return this;
    }

    public ResponseHtmlBuilder addProducts(List<Product> products) {
        for (Product product: products) {
            addProduct(product);
        }

        return this;
    }

    public String build() {
        StringBuilder builder = new StringBuilder();
        for (String line : lines) {
            builder.append(line).append("\n");
        }
        return OPEN + "\n" +
                (!header.isEmpty() ? header + "\n" : "") +
                builder.toString() + CLOSE;
    }


}
