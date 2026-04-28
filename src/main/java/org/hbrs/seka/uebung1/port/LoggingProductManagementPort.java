package org.hbrs.seka.uebung1.port;


import org.hbrs.seka.uebung1.internal.logging.Logger;
import org.hbrs.seka.uebung1.api.ProductManagementInt;
import org.hbrs.seka.uebung1.model.Product;

public class LoggingProductManagementPort implements ProductManagementInt {

    private final ProductManagementInt delegate;
    private final Logger logger;

    public LoggingProductManagementPort(ProductManagementInt delegate,
                                        Logger logger) {
        this.delegate = delegate;
        this.logger = logger;
    }

    @Override
    public void openSession() {
        logger.log("Zugriff auf ProductManagement über Methode openSession");
        delegate.openSession();
    }

    @Override
    public Product[] getProductByName(String name) {
        logger.log(
                "Zugriff auf ProductManagement über Methode getProductByName. " +
                        "Suchwort: " + name
        );
        return delegate.getProductByName(name);
    }

    @Override
    public Product[] getAllProducts() {
        logger.log("Zugriff auf ProductManagement über Methode getAllProducts");
        return delegate.getAllProducts();
    }

    @Override
    public void saveProduct(Product product) {
        logger.log("Zugriff auf ProductManagement über Methode saveProduct");
        delegate.saveProduct(product);
    }

    @Override
    public void deleteProduct(Product product) {
        logger.log("Zugriff auf ProductManagement über Methode deleteProduct");
        delegate.deleteProduct(product);
    }

    @Override
    public void deleteAllProducts() {
        logger.log("Zugriff auf ProductManagement über Methode deleteAllProducts");
        delegate.deleteAllProducts();
    }

    @Override
    public void updateProduct(Product product) {
        logger.log("Zugriff auf ProductManagement über Methode updateProduct");
        delegate.updateProduct(product);
    }

    @Override
    public void closeSession() {
        logger.log("Zugriff auf ProductManagement über Methode closeSession");
        delegate.closeSession();
    }
}

