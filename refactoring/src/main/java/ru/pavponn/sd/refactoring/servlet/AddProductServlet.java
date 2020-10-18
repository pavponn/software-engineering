package ru.pavponn.sd.refactoring.servlet;

import ru.pavponn.sd.refactoring.dao.ProductDao;
import ru.pavponn.sd.refactoring.dao.ProductDaoReadWrite;
import ru.pavponn.sd.refactoring.models.Product;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * @author akirakozov
 */
public class AddProductServlet extends HttpServlet {
    private final ProductDaoReadWrite productDao;

    public AddProductServlet() {
        this("test.db");
    }

    public AddProductServlet(String dbName) {
        this.productDao = new ProductDao(dbName);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        long price = Long.parseLong(request.getParameter("price"));
        Product product = new Product(name, price);
        response.setContentType("text/html");
        if (productDao.addProduct(product)) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("OK");
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("FAIL");
        }
    }
}
