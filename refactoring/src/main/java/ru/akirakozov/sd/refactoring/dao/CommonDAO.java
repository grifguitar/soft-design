package ru.akirakozov.sd.refactoring.dao;

import ru.akirakozov.sd.refactoring.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommonDAO {
    private static final String DB_URL = "jdbc:sqlite:test.db";

    private static void executeUpdate(String sql) {
        try (Connection c = DriverManager.getConnection(DB_URL)) {
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static List<Product> executeQuery(String sql, boolean isScalar) {
        List<Product> result = new ArrayList<>();
        try (Connection c = DriverManager.getConnection(DB_URL)) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                if (isScalar) {
                    result.add(new Product("", rs.getInt(1)));
                } else {
                    result.add(new Product(rs.getString("name"), rs.getInt("price")));
                }
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void createProductTable() {
        String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " PRICE          INT     NOT NULL)";
        executeUpdate(sql);
    }

    public static void insertIntoProductTable(Product product) {
        String sql = "INSERT INTO PRODUCT " +
                "(NAME, PRICE) VALUES (\"" + product.getName() + "\"," + product.getPrice() + ")";
        executeUpdate(sql);
    }

    public static List<Product> selectAllFromProductTable() {
        String sql = "SELECT * FROM PRODUCT";
        return executeQuery(sql, false);
    }

    public static List<Product> selectFromProductTableByMax() {
        String sql = "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1";
        return executeQuery(sql, false);
    }

    public static List<Product> selectFromProductTableByMin() {
        String sql = "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1";
        return executeQuery(sql, false);
    }

    public static int selectFromProductTableBySum() {
        String sql = "SELECT SUM(price) FROM PRODUCT";
        return (int) executeQuery(sql, true).get(0).getPrice();
    }

    public static int selectFromProductTableByCount() {
        String sql = "SELECT COUNT(*) FROM PRODUCT";
        return (int) executeQuery(sql, true).get(0).getPrice();
    }
}
