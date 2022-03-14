package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.dto.CommonDTO;
import ru.akirakozov.sd.refactoring.model.Product;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AbstractHtmlServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CommonDTO data = action(request);
        write(response, data);
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    protected abstract CommonDTO action(HttpServletRequest request);

    private static void write(HttpServletResponse response, CommonDTO data) throws IOException {
        if (data.getProducts() != null || data.getValue() != null) {
            response.getWriter().println("<html><body>");
            response.getWriter().print(data.getMessage());
            if (data.getProducts() != null) {
                for (Product product : data.getProducts()) {
                    response.getWriter().println(product.getName() + "\t" + product.getPrice() + "</br>");
                }
            } else {
                response.getWriter().println(data.getValue());
            }
            response.getWriter().println("</body></html>");
        } else {
            response.getWriter().print(data.getMessage());
        }
    }
}
