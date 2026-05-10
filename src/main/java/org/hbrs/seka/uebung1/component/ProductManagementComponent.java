package org.hbrs.seka.uebung1.component;

import org.hbrs.seka.uebung1.Caching;
import org.hbrs.seka.uebung1.internal.logging.ConsoleLogger;
import org.hbrs.seka.uebung1.internal.logging.Logger;
import org.hbrs.seka.uebung1.internal.cache.NoOpCache;
import org.hbrs.seka.uebung1.api.ProductManagementInt;
import org.hbrs.seka.uebung1.port.LoggingProductManagementPort;
import org.hbrs.seka.uebung1.internal.ProductController;
import org.hbrs.seka.uebung1.internal.persistence.ProductRepository;
import org.hbrs.seka.uebung1.port.ProductManagementPort;

import java.util.Objects;

public class ProductManagementComponent {

    public static ProductManagementInt create() {
        return create(null);
    }

    public static ProductManagementInt create(Caching cache) {
        cache = Objects.requireNonNullElse(cache, new NoOpCache());

        ProductRepository repo = new ProductRepository();

        ProductController controller =
                new ProductController(
                        repo,
                        cache
                );

        // Fach-Port
        ProductManagementInt basePort =
                new ProductManagementPort(controller);

        // Logging-Port davor
        Logger logger = new ConsoleLogger();

        return new LoggingProductManagementPort(basePort, logger);
    }

}