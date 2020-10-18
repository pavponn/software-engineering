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
public class QueryServlet extends HttpServlet {
    private final ProductDaoReadWrite productDao;

    public QueryServlet() {
        this("test.db");
    }

    public QueryServlet(String dbName) {
        this.productDao = new ProductDao(dbName);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");
        if ("max".equals(command)) {
            String responseString = responseBuilder("<h1>Product with max price: </h1>")
                    .addProduct(productDao.getMaxPriceProduct())
                    .build();
            response.getWriter().println(responseString);
        } else if ("min".equals(command)) {
            String responseString = responseBuilder("<h1>Product with min price: </h1>")
                    .addProduct(productDao.getMinPriceProduct())
                    .build();
            response.getWriter().println(responseString);
        } else if ("sum".equals(command)) {
            String responseString = responseBuilder("Summary price: ")
                    .addLine(productDao.getSumPrices())
                    .build();
            response.getWriter().println(responseString);
        } else if ("count".equals(command)) {
            String responseString = responseBuilder("Number of products: ")
                    .addLine(productDao.getCount())
                    .build();
            response.getWriter().println(responseString);
        } else {
            response.getWriter().println("Unknown command: " + command);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
