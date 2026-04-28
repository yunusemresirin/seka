package org.hbrs.seka.uebung1.internal;

import org.hbrs.seka.uebung1.Caching;
import org.hbrs.seka.uebung1.internal.persistence.ProductRepository;
import org.hbrs.seka.uebung1.model.Product;

import java.util.List;

public class ProductController {

    private final ProductRepository repository;
    private final Caching cache;

    public ProductController(ProductRepository repository, Caching cache) {
        this.repository = repository;
        this.cache = cache;
    }

    public void saveProduct(Product product) {
        repository.save(product);

        // --- Cache-Update ---
        // das Produkt wird aus der DB erneut gelesen,
        // da die ID iterativ in der DB gesetzt wird
        Product[] products = repository.findByName(product.getName());
        // Produkte werden überschrieben
        cache.cacheResult(product.getName(), List.of(products));
    }

    public Product[] getAllProducts() {
        Product[] products;

        // --- Cache-Check ---
        var cached = cache.getCacheSnapshot();
        if (cached != null) {
            products = cached.values().stream()
                    .flatMap(List::stream)                        // Alle Listen zusammenführen mittels Methodenreferenz
                    .filter(o -> o instanceof Product) // Prüfen, ob es ein Product ist
                    .map(o -> (Product) o)             // Sicherer Cast zu Product
                    .toArray(Product[]::new);                     // Mittels Methodenreferenz zu Product[] (size → new Product[size])

            return products;
        }

        // --- Cache-Update ---
        // alle Produkte werden aus der DB gelesen und in den Cache geschrieben
        products = repository.findAll();
        for(Product p : products) cache.cacheResult(p.getName(), List.of(p));
        return products;
    }

    public Product[] getProductByName(String name) {
        // Cache-Check
        var cached = cache.getCachedResult(name);
        if (cached != null) {
            // hier wird davon ausgegangen, dass der Cache nur Produkte enthält
            return cached.toArray(Product[]::new);
        }

        // --- Cache-Update ---
        // falls die Produkte nicht im Cache sind,
        // werden sie aus der DB gelesen und in den Cache geschrieben
        Product[] products = repository.findByName(name);
        cache.cacheResult(name, List.of(products));
        return products;
    }

    public void updateProduct(Product product) {
        repository.update(product);

        // --- Cache-Update ---
        Product[] products = repository.findByName(product.getName());
        cache.cacheResult(product.getName(), List.of(products));
    }

    public void deleteProduct(Product product) {
        repository.delete(product);

        // --- Cache-Update ---
        // es wird allerdings nicht ganz funktionieren, da der Vergleich "equals" nur
        // auf dem Namen und dem Preis basiert, und daher nicht eindeutig ist.
        var cached = cache.getCacheSnapshot();
        if (cached != null)
            cached.entrySet().stream()
                // 1. Behalte alle Produkte im Stream
                .filter(entry -> entry.getValue().stream().allMatch(Product.class::isInstance))
                // 2. Iteriere jeweils durch die Entries, entferne das entsprechende Produkt
                // und schreibe das neue Ergebnis in den Cache
                .forEach(entry -> {
                    List<Product> filtered = entry.getValue().stream()
                            .map(Product.class::cast)
                            .filter(p -> !p.equals(product))
                            .toList();

                    cache.cacheResult(entry.getKey(), filtered);
                });
    }

    public void deleteAllProducts() {
        repository.deleteAll();

        // --- Cache-Update ---
        var cached = cache.getCacheSnapshot();
        if (cached != null) {
            cached.entrySet().stream()
                    // 1. Entferne alle Entries, deren Value null ist
                    .filter(entry -> entry.getValue() != null)
                    // 2. Entferne alle Entries, deren Value-Liste leer ist
                    .filter(entry -> !entry.getValue().isEmpty())
                    // 3. Entferne die Entries, deren Value Produkte sind
                    .filter(entry -> !entry.getValue().stream().allMatch(Product.class::isInstance))
                    // 4. Schreibe das neue Ergebnis in den Cache
                    .forEach(entry -> cache.cacheResult(entry.getKey(), entry.getValue()));
        }
    }

    public void openSession() {
        repository.openConnection();
    }

    public void closeSession() {
        repository.closeConnection();
    }
}
