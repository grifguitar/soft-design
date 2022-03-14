package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.dao.CommonDAO;
import ru.akirakozov.sd.refactoring.dto.CommonDTO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends AbstractHtmlServlet {
    @Override
    protected CommonDTO getCommonDTO(HttpServletRequest request) {
        return new CommonDTO(
                "",
                CommonDAO.selectAllFromProductTable()
        );
    }
}
