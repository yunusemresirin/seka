package org.hbrs.seka.uebung1.client;

import org.hbrs.seka.uebung1.api.ProductManagementInt;
import org.hbrs.seka.uebung1.component.ProductManagementComponent;
import org.hbrs.seka.uebung1.model.Product;
import org.hbrs.seka.uebung2.core.component.annotations.Start;
import org.hbrs.seka.uebung2.core.component.annotations.Stop;

public class ProductManagementClient {

    private volatile boolean running = true;

    @Start
    public void start() {
        ProductManagementInt productManagement = ProductManagementComponent.create();
        productManagement.openSession();

        productManagement.saveProduct(new Product(1, "Tastatur", 49.99));
        productManagement.saveProduct(new Product(2, "Maus", 19.99));

        while (running && !Thread.currentThread().isInterrupted()) {
            Product[] products = productManagement.getAllProducts();
            System.out.println("Produkte: " + products.length);

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        productManagement.closeSession();
    }

    @Stop
    public void stop() {
        running = false;
    }
}