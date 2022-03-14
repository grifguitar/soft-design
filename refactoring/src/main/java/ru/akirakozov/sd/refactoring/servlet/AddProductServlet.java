package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.dao.CommonDAO;
import ru.akirakozov.sd.refactoring.dto.CommonDTO;
import ru.akirakozov.sd.refactoring.model.Product;

import javax.servlet.http.HttpServletRequest;

/**
 * @author akirakozov
 */
public class AddProductServlet extends AbstractHtmlServlet {
    @Override
    protected CommonDTO action(HttpServletRequest request) {
        Product product = new Product(
                request.getParameter("name"),
                Long.parseLong(request.getParameter("price"))
        );

        CommonDAO.insertIntoProductTable(product);

        return new CommonDTO("OK\n");
    }
}
