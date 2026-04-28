package org.hbrs.seka.uebung1.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

// Internal Packages
import org.hbrs.seka.uebung1.model.Product;
import org.hbrs.seka.uebung1.internal.persistence.DatabaseConnection;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ConnectionTest {

    private Connection connection;

    @BeforeEach
    public void setup()  {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {
            connection = databaseConnection.getConnection();

            String sql = "CREATE TABLE IF NOT EXISTS products ("
                + "id INT PRIMARY KEY AUTO_INCREMENT, "
                + "name VARCHAR(255) NOT NULL, "
                + "price DOUBLE NOT NULL)";

            Statement stmt = connection.createStatement();
            stmt.execute(sql);
            System.out.println("Table created successfully.");

        } catch (SQLException e) {
            fail("Could not create table: " + e.getMessage());
        }
    }

    @Test
    public void roundTrip() {
        // Lesen des Produkts aus der Datenbank (READ)
        List<Product> products = readProducts();
        for(Product p : products) { System.out.println(p); }
        // Anzahl der Produkte
        System.out.println("current size: " + products.size());

        // Erzeugung eines neuen Produkts (CREATE)
        Product productTarget = insertProduct();

        // Vergleich des Produkts mit dem erwarteten Produkt (Assertion)
        Product productActual = products.get(0);
        assertEquals(productTarget, productActual);

        // Ändern des Produktes
        updateProduct(productActual);
        products = readProducts();
        for(Product p : products) { System.out.println(p); }

        // Löschen des Produktes
        deleteProduct(productActual);
        products = readProducts();
        // Anzahl der Produkte
        System.out.println("current size: " + products.size());
        // Aktuelle Produkte anzeigen
        for(Product p : products) { System.out.println(p); }

    }

    @AfterEach
    public void deleteStuff() {
        // SQL für die Löschung der Tabelle (Vermeidung von Datenmüll)
         String sql = "DROP TABLE products CASCADE CONSTRAINTS";
        try {
            // Diese Verbindung könnte im Rahmen der Übung Nr. 1 auch über die Methode closeConnection() geschlossen werden
            // (vgl. Interface ProductManagementInt)
            PreparedStatement pstmt = this.connection.prepareStatement(sql);
            pstmt.executeUpdate();
            this.connection.close();
        } catch (SQLException e) {
            fail("Could not delete table products: " + e.getMessage());
        }
    }

    private Product insertProduct() {
        String sql1 = "INSERT INTO products (name, price) VALUES (?, ?)";
        Product productTarget = new Product(1, "My Motor 1.0", 100.0);

        try {
            PreparedStatement pstmt = this.connection.prepareStatement(sql1);
            pstmt.setString(1, productTarget.getName());
            pstmt.setDouble(2, productTarget.getPrice());
            pstmt.executeUpdate();
            System.out.println("Product inserted successfully.");
        } catch (SQLException e2) {
            fail("Could not insert product: " + e2.getMessage());
        }

        return productTarget;
    }

    private List<Product> readProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try {
            PreparedStatement pstmt = this.connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price")
                ));
            }
        } catch (Exception e) {
            fail("Could not read products: " + e.getMessage());
        }
        return products;
    }

    private void updateProduct(Product product) {
        String sql = "UPDATE products SET name = ?, price = ? WHERE id = ?";

        try {
            PreparedStatement pstmt = this.connection.prepareStatement(sql);
            pstmt.setString(1, "My Motor 2.0");
            pstmt.setDouble(2, 50.0);
            pstmt.setInt(3, product.getId());
            pstmt.executeUpdate();
            System.out.println("Product updated successfully.");
        } catch (SQLException e) {
            fail("Could not update product: " + e.getMessage());
        }
    }

    private void deleteProduct(Product product) {
        String sql = "DELETE FROM products WHERE id = ?";

        try {
            PreparedStatement pstmt = this.connection.prepareStatement(sql);
            pstmt.setInt(1, product.getId());
            pstmt.executeUpdate();
            System.out.println("Product deleted successfully.");
        } catch (SQLException e) {
            fail("Could not delete product: " + e.getMessage());
        }
    }

}