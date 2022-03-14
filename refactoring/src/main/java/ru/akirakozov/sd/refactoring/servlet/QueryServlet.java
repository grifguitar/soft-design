package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.dao.CommonDAO;
import ru.akirakozov.sd.refactoring.model.Product;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");
        boolean isAnswer = false;

        if ("max".equals(command)) {
            response.getWriter().println("<html><body>");
            response.getWriter().println("<h1>Product with max price: </h1>");
            List<Product> products = CommonDAO.selectFromProductTableByMax();
            for (Product product : products) {
                response.getWriter().println(product.name + "\t" + product.price + "</br>");
            }
            response.getWriter().println("</body></html>");
            isAnswer = true;
        }
        if ("min".equals(command)) {
            response.getWriter().println("<html><body>");
            response.getWriter().println("<h1>Product with min price: </h1>");
            List<Product> products = CommonDAO.selectFromProductTableByMin();
            for (Product product : products) {
                response.getWriter().println(product.name + "\t" + product.price + "</br>");
            }
            response.getWriter().println("</body></html>");
            isAnswer = true;
        }
        if ("sum".equals(command)) {
            response.getWriter().println("<html><body>");
            response.getWriter().println("Summary price: ");
            int result = CommonDAO.selectFromProductTableBySum();
            response.getWriter().println(result);
            response.getWriter().println("</body></html>");
            isAnswer = true;
        }
        if ("count".equals(command)) {
            response.getWriter().println("<html><body>");
            response.getWriter().println("Number of products: ");
            int result = CommonDAO.selectFromProductTableByCount();
            response.getWriter().println(result);
            response.getWriter().println("</body></html>");
            isAnswer = true;
        }
        if (!isAnswer) {
            response.getWriter().println("Unknown command: " + command);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
