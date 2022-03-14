package ru.akirakozov.sd.refactoring.dao;

import ru.akirakozov.sd.refactoring.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommonDAO {
    public static void createProductTable() throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

    public static void insertIntoProductTable(String name, long price) throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "INSERT INTO PRODUCT " +
                    "(NAME, PRICE) VALUES (\"" + name + "\"," + price + ")";
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

    public static List<Product> selectAllFromProductTable() throws SQLException {
        List<Product> result = new ArrayList<>();

        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT");

            while (rs.next()) {
                String name = rs.getString("name");
                int price = rs.getInt("price");
                result.add(new Product(name, price));
            }

            rs.close();
            stmt.close();
        }

        return result;
    }

    public static List<Product> selectFromProductTableByMax() throws SQLException {
        List<Product> result = new ArrayList<>();

        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1");

            while (rs.next()) {
                String name = rs.getString("name");
                int price = rs.getInt("price");
                result.add(new Product(name, price));
            }

            rs.close();
            stmt.close();
        }

        return result;
    }

    public static List<Product> selectFromProductTableByMin() throws SQLException {
        List<Product> result = new ArrayList<>();

        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1");

            while (rs.next()) {
                String name = rs.getString("name");
                int price = rs.getInt("price");
                result.add(new Product(name, price));
            }

            rs.close();
            stmt.close();
        }

        return result;
    }

    public static int selectFromProductTableBySum() throws SQLException {
        int result = 0;

        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT SUM(price) FROM PRODUCT");

            if (rs.next()) {
                result = rs.getInt(1);
            }

            rs.close();
            stmt.close();
        }

        return result;
    }

    public static int selectFromProductTableByCount() throws SQLException {
        int result = 0;

        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM PRODUCT");

            if (rs.next()) {
                result = rs.getInt(1);
            }

            rs.close();
            stmt.close();
        }

        return result;
    }
}
