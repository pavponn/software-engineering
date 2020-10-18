package ru.pavponn.sd.refactoring.servlet;

import ru.pavponn.sd.refactoring.dao.ProductDao;
import ru.pavponn.sd.refactoring.dao.ProductDaoReadWrite;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.pavponn.sd.refactoring.response.ResponseHtmlBuilder.responseBuilder;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {
    private final ProductDaoReadWrite productDao;

    public GetProductsServlet() {
        this("test.db");
    }

    public GetProductsServlet(String dbName) {
        this.productDao = new ProductDao(dbName);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String responseString = responseBuilder()
                .addProducts(productDao.getAllProducts())
                .build();
        response.getWriter().println(responseString);
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
