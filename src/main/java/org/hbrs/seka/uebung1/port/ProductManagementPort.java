package org.hbrs.seka.uebung1.port;

import org.hbrs.seka.uebung1.api.ProductManagementInt;
import org.hbrs.seka.uebung1.model.Product;
import org.hbrs.seka.uebung1.internal.ProductController;

public class ProductManagementPort implements ProductManagementInt {

    private final ProductController controller;
    private LifecycleState state = LifecycleState.CLOSED;

    public ProductManagementPort(ProductController controller) {
        this.controller = controller;
    }

    @Override
    public void openSession() {
        controller.openSession();
        state = LifecycleState.OPEN;
    }

    @Override
    public void closeSession() {
        controller.closeSession();
        state = LifecycleState.CLOSED;
    }

    @Override
    public Product[] getProductByName(String name) {
        if (state == LifecycleState.CLOSED) {
            throw new IllegalStateException("Session not opened");
        }

        return controller.getProductByName(name);
    }

    @Override
    public Product[] getAllProducts() {
        if (state == LifecycleState.CLOSED) {
            throw new IllegalStateException("Session not opened");
        }

        return controller.getAllProducts();
    }

    @Override
    public void saveProduct(Product product) {
        if (state == LifecycleState.CLOSED) {
            throw new IllegalStateException("Session not opened");
        }

        controller.saveProduct(product);
    }

    @Override
    public void deleteProduct(Product product) {
        if (state == LifecycleState.CLOSED) {
            throw new IllegalStateException("Session not opened");
        }

        controller.deleteProduct(product);
    }

    @Override
    public void deleteAllProducts() {
        if (state == LifecycleState.CLOSED) {
            throw new IllegalStateException("Session not opened");
        }

        controller.deleteAllProducts();
    }

    @Override
    public void updateProduct(Product product) {
        if (state == LifecycleState.CLOSED) {
            throw new IllegalStateException("Session not opened");
        }

        controller.updateProduct(product);
    }
}
