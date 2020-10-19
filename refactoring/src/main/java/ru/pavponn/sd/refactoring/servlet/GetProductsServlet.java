package ru.pavponn.sd.refactoring.servlet;

import ru.pavponn.sd.refactoring.dao.ProductDaoRW;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.pavponn.sd.refactoring.response.ResponseHtmlBuilder.responseBuilder;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {
    private final ProductDaoRW productDao;

    public GetProductsServlet(ProductDaoRW productDao) {
        this.productDao = productDao;
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
