package ru.pavponn.sd.refactoring.servlet;

import ru.pavponn.sd.refactoring.dao.ProductDao;
import ru.pavponn.sd.refactoring.dao.ProductDaoReadWrite;
import ru.pavponn.sd.refactoring.models.Product;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        Product product = new Product(request.getParameter("name"), Long.parseLong(request.getParameter("price")));
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
