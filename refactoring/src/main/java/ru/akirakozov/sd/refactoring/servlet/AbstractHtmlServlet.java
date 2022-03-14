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
        write(response, getCommonDTO(request));
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    protected abstract CommonDTO getCommonDTO(HttpServletRequest request);

    private static void write(HttpServletResponse response, CommonDTO data) throws IOException {
        if (data.products != null || data.value != null) {
            response.getWriter().println("<html><body>");
            response.getWriter().print(data.message);
            if (data.products != null) {
                for (Product product : data.products) {
                    response.getWriter().println(product.name + "\t" + product.price + "</br>");
                }
            } else {
                response.getWriter().println(data.value);
            }
            response.getWriter().println("</body></html>");
            return;
        }
        response.getWriter().print(data.message);
    }
}
