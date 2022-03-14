package ru.akirakozov.sd.refactoring.servlet;

import org.junit.After;
import org.junit.Before;

import javax.servlet.http.HttpServlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static ru.akirakozov.sd.refactoring.servlet.Utils.*;

public abstract class AbstractTest {

    // fields:

    protected HttpServlet httpServlet;

    // abstract methods:

    protected abstract void init();

    // public methods:

    @Before
    public void setUp() throws Exception {
        init();
        cleanDatabase();
    }

    @After
    public void tearDown() throws Exception {
        cleanDatabase();
    }

    // database methods:

    protected void addToDatabase(Store product) throws SQLException {
        addToDatabase(product.getName(), product.getPrice());
    }

    private void addToDatabase(String name, String price) throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "INSERT INTO PRODUCT " +
                    "(NAME, PRICE) VALUES (\"" + name + "\"," + price + ")";
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

    protected void cleanDatabase() throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "DELETE FROM PRODUCT";
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

}
