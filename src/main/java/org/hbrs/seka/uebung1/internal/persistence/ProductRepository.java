package org.hbrs.seka.uebung1.internal.persistence;

import org.hbrs.seka.uebung1.model.Product;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    private static Connection connection;

    // ------- Create -------

    public void save(@NotNull Product product) {
        String sql1 = "INSERT INTO products (name, price) VALUES (?, ?)";

        try {
            PreparedStatement pstmt = connection.prepareStatement(
                    sql1
//                    ,Statement.RETURN_GENERATED_KEYS
            );
            pstmt.setString(1, product.getName());
            pstmt.setDouble(2, product.getPrice());
            pstmt.executeUpdate();

//            ID des Produktes kann u.U. aus dem ResultSet gelesen werden
//            ResultSet generatedKeys = pstmt.getGeneratedKeys();
//
//            if (generatedKeys.next()) {
//                int generatedId = generatedKeys.getInt(1);
//                return new Product(generatedId, product.getName(), product.getPrice());
//            }
//
//            throw new SQLException("Creating product failed, no ID obtained.");

        } catch (SQLException e2) {
            e2.printStackTrace();
        }
    }

    // ------- Read -------

    public Product[] findAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products.toArray(new Product[0]);
    }

    public Product[] findByName(String name) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE name = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products.toArray(new Product[0]);
    }

    // ------- Update -------

    public void update(Product product) {
        String sql = "UPDATE products SET name = ?, price = ? WHERE id = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, product.getName());
            pstmt.setDouble(2, product.getPrice());
            pstmt.setInt(3, product.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ------- Delete -------

    public void delete(Product product) {
        // Bei Constraints kann es zu Fehlern kommen, wenn das Produkt noch in anderen Tabellen referenziert wird
        String sql = "DELETE FROM products WHERE id = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, product.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAll() {
        String sql = "DROP TABLE products";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void openConnection() {
        try {
            DatabaseConnection.openSession();
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        DatabaseConnection.closeSession();
        connection = null;
    }

}
