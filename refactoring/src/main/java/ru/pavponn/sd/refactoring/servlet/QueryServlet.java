package ru.pavponn.sd.refactoring.servlet;

import ru.pavponn.sd.refactoring.dao.ProductDao;
import ru.pavponn.sd.refactoring.dao.ProductDaoReadWrite;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.pavponn.sd.refactoring.commands.Commands.*;
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
        String responseString;
        switch (command) {
            case MAX:
                responseString = responseBuilder("<h1>Product with max price: </h1>")
                        .addProduct(productDao.getMaxPriceProduct())
                        .build();
                break;
            case MIN:
                responseString = responseBuilder("<h1>Product with min price: </h1>")
                        .addProduct(productDao.getMinPriceProduct())
                        .build();
                break;
            case SUM:
                responseString = responseBuilder("Summary price: ")
                        .addLine(productDao.getSumPrices())
                        .build();
                break;
            case COUNT:
                responseString = responseBuilder("Number of products: ")
                        .addLine(productDao.getCount())
                        .build();
                break;
            default:
                responseString = "Unknown command: " + command;
        }
        response.getWriter().println(responseString);
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
