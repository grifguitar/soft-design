package ru.akirakozov.sd.refactoring.servlet;

import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static ru.akirakozov.sd.refactoring.servlet.Utils.*;

public class AddProductServletTest extends AbstractTest {

    private void commonAddProductTest() throws IOException, ServletException {
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        when(requestMock.getParameter(NAME)).thenReturn(Store.PHONE.getName());
        when(requestMock.getParameter(PRICE)).thenReturn(Store.PHONE.getPrice());
        when(requestMock.getMethod()).thenReturn(GET_METHOD);

        HttpServletResponse responseMock = mock(HttpServletResponse.class);
        when(responseMock.getWriter()).thenReturn(new StringPrintWriter());

        httpServlet.service(requestMock, responseMock);

        assertEquals("OK" + EOLN, responseMock.getWriter().toString());
    }

    @Test
    public void test0() throws ServletException, IOException {
        commonAddProductTest();
    }

    @Override
    protected void init() {
        httpServlet = new AddProductServlet();
    }
}
