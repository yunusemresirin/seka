package org.hbrs.seka.uebung1.test;

import org.hbrs.seka.uebung1.api.ProductManagementInt;
import org.hbrs.seka.uebung1.component.ProductManagementComponent;
import org.junit.jupiter.api.*;

// Internal Packages
import org.hbrs.seka.uebung1.model.Product;
import org.hbrs.seka.uebung1.internal.persistence.DatabaseConnection;

public class ConnectionTest {

    private ProductManagementInt productManagement;

    @BeforeEach
    void setUp() {
        productManagement = ProductManagementComponent.create(null);
        productManagement.openSession();
    }

    @AfterEach
    void tearDown() {
        productManagement.deleteAllProducts();
        DatabaseConnection.closeSession();
    }


    @Test
    void createAndReadProduct() {
        Product product = new Product(1, "Laptop", 999.99);

        productManagement.saveProduct(product);

        Product[] result = productManagement.getProductByName("Laptop");

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Laptop", result[0].getName());
    }

    @Test
    void updateProduct() {
        Product product = new Product(1, "Mouse", 20.00);
        productManagement.saveProduct(product);

        Product updated = new Product(1, "Gaming Mouse", 49.99);
        productManagement.updateProduct(updated);

        Product[] result = productManagement.getProductByName("Gaming Mouse");

        Assertions.assertEquals("Gaming Mouse", result[0].getName());
        Assertions.assertEquals(49.99, result[0].getPrice());
    }

    @Test
    void deleteProduct() {
        Product product = new Product(3, "Keyboard", 79.99);

        productManagement.saveProduct(product);

        productManagement.deleteProduct(product);

        Assertions.assertEquals(0, productManagement.getProductByName("Keyboard").length);
    }

}