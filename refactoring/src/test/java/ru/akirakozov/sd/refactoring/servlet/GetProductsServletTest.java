package ru.akirakozov.sd.refactoring.servlet;

import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static ru.akirakozov.sd.refactoring.servlet.Utils.*;

public class GetProductsServletTest extends AbstractTest {

    private void commonGetProductsTest(List<Store> products) throws SQLException, ServletException, IOException {
        for (Store product : products) {
            addToDatabase(product);
        }

        StringBuilder expectedBody = new StringBuilder("");
        for (Store product : products) {
            expectedBody.append(product.getName());
            expectedBody.append(TAB);
            expectedBody.append(product.getPrice());
            expectedBody.append(BR_TAG);
            expectedBody.append(EOLN);
        }

        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        when(requestMock.getMethod()).thenReturn(GET_METHOD);

        HttpServletResponse responseMock = mock(HttpServletResponse.class);
        when(responseMock.getWriter()).thenReturn(new StringPrintWriter());

        httpServlet.service(requestMock, responseMock);

        assertEquals(HTML_BODY_OPEN_TAG + EOLN + expectedBody + BODY_HTML_CLOSE_TAG + EOLN,
                responseMock.getWriter().toString());
    }

    @Test
    public void test0() throws SQLException, ServletException, IOException {
        commonGetProductsTest(List.of());
    }

    @Test
    public void test1() throws SQLException, ServletException, IOException {
        commonGetProductsTest(List.of(Store.PHONE));
    }

    @Test
    public void test2() throws SQLException, ServletException, IOException {
        commonGetProductsTest(List.of(Store.PHONE, Store.GUITAR, Store.PIANO, Store.DRUMS));
    }

    @Test
    public void test3() throws SQLException, ServletException, IOException {
        commonGetProductsTest(
                List.of(Store.PHONE, Store.GUITAR, Store.GUITAR2, Store.PIANO, Store.PIANO2, Store.DRUMS));
    }

    @Override
    protected void init() {
        httpServlet = new GetProductsServlet();
    }
}
