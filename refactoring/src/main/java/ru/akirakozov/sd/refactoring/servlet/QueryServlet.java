package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.dao.CommonDAO;
import ru.akirakozov.sd.refactoring.dto.CommonDTO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author akirakozov
 */
public class QueryServlet extends AbstractHtmlServlet {
    @Override
    protected CommonDTO getCommonDTO(HttpServletRequest request) {
        String command = request.getParameter("command");
        switch (command) {
            case "max":
                return new CommonDTO(
                        "<h1>Product with max price: </h1>\n",
                        CommonDAO.selectFromProductTableByMax()
                );
            case "min":
                return new CommonDTO(
                        "<h1>Product with min price: </h1>\n",
                        CommonDAO.selectFromProductTableByMin()
                );
            case "sum":
                return new CommonDTO(
                        "Summary price: \n",
                        CommonDAO.selectFromProductTableBySum()
                );
            case "count":
                return new CommonDTO(
                        "Number of products: \n",
                        CommonDAO.selectFromProductTableByCount()
                );
            default:
                return new CommonDTO("Unknown command: " + command + "\n");
        }
    }
}
