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

public class QueryServletTest extends AbstractTest {

    private static Store find(List<Store> products, int value) {
        return products.stream()
                .filter(product -> Integer.parseInt(product.getPrice()) == value)
                .findFirst()
                .orElse(null);
    }

    private void commonQueryTest(Commands command, List<Store> products) throws SQLException, ServletException, IOException {
        for (Store product : products) {
            addToDatabase(product);
        }

        String expectedBody = "";

        if (command == Commands.SUM) {
            int sum = products.stream()
                    .map(product -> Integer.parseInt(product.getPrice()))
                    .reduce(0, Integer::sum);
            expectedBody = "Summary price: " + EOLN + sum + EOLN;
        }

        if (command == Commands.COUNT) {
            int cnt = products.size();
            expectedBody = "Number of products: " + EOLN + cnt + EOLN;
        }

        if (command == Commands.MAX) {
            int max = products.stream()
                    .map(product -> Integer.parseInt(product.getPrice()))
                    .max(Integer::compare)
                    .orElse(Integer.MIN_VALUE);
            Store bestProduct = find(products, max);
            String name = bestProduct != null ? bestProduct.getName() + TAB : "";
            String price = bestProduct != null ? bestProduct.getPrice() + BR_TAG + EOLN : "";
            expectedBody = "<h1>Product with max price: </h1>" + EOLN + name + price;
        }

        if (command == Commands.MIN) {
            int min = products.stream()
                    .map(product -> Integer.parseInt(product.getPrice()))
                    .min(Integer::compare)
                    .orElse(Integer.MAX_VALUE);
            Store bestProduct = find(products, min);
            String name = bestProduct != null ? bestProduct.getName() + TAB : "";
            String price = bestProduct != null ? bestProduct.getPrice() + BR_TAG + EOLN : "";
            expectedBody = "<h1>Product with min price: </h1>" + EOLN + name + price;
        }

        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        when(requestMock.getParameter(COMMAND)).thenReturn(command.getName());
        when(requestMock.getMethod()).thenReturn(GET_METHOD);

        HttpServletResponse responseMock = mock(HttpServletResponse.class);
        when(responseMock.getWriter()).thenReturn(new StringPrintWriter());

        httpServlet.service(requestMock, responseMock);

        assertEquals(HTML_BODY_OPEN_TAG + EOLN + expectedBody + BODY_HTML_CLOSE_TAG + EOLN,
                responseMock.getWriter().toString());
    }

    @Test
    public void test0() throws ServletException, SQLException, IOException {
        commonQueryTest(Commands.SUM,
                List.of(Store.PHONE, Store.GUITAR, Store.GUITAR2, Store.PIANO, Store.PIANO2, Store.DRUMS));
    }

    @Test
    public void test1() throws ServletException, SQLException, IOException {
        commonQueryTest(Commands.COUNT,
                List.of(Store.PHONE, Store.GUITAR, Store.GUITAR2, Store.PIANO, Store.PIANO2, Store.DRUMS));
    }

    @Test
    public void test2() throws ServletException, SQLException, IOException {
        commonQueryTest(Commands.MAX,
                List.of(Store.PHONE, Store.GUITAR, Store.GUITAR2, Store.PIANO, Store.PIANO2, Store.DRUMS));
    }

    @Test
    public void test3() throws ServletException, SQLException, IOException {
        commonQueryTest(Commands.MIN,
                List.of(Store.PHONE, Store.GUITAR, Store.GUITAR2, Store.PIANO, Store.PIANO2, Store.DRUMS));
    }

    @Test
    public void test4() throws ServletException, SQLException, IOException {
        commonQueryTest(Commands.SUM, List.of());
    }

    @Test
    public void test5() throws ServletException, SQLException, IOException {
        commonQueryTest(Commands.COUNT, List.of());
    }

    @Test
    public void test6() throws ServletException, SQLException, IOException {
        commonQueryTest(Commands.MAX, List.of());
    }

    @Test
    public void test7() throws ServletException, SQLException, IOException {
        commonQueryTest(Commands.MIN, List.of());
    }

    @Override
    protected void init() {
        httpServlet = new QueryServlet();
    }
}
