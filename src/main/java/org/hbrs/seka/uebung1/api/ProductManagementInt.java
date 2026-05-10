package org.hbrs.seka.uebung1.api;

import org.hbrs.seka.uebung1.model.Product;

/**
 * Spezifikation des Interfaces ProductManagementInt:
 * <p>
 * 1.
 * Zunächst MUSS ein externer Client (außerhalb der Komponente!) mit der Methode
 * openConnection() eine Session explizit öffnen!" );
 * <p>
 * 2.
 * Methoden zur Suche, Einfügen usw. können beliebig ausgeführt werden.
 * <p>
 * 3.
 * Dann MUSS ein externer Client mit der Methode closeConnection() die Session explizit schließen!
 */

public interface ProductManagementInt {
    // Auswahl von CRUD-Methoden (weitere können hinzugefügt werden)
    Product[] getProductByName(String name);
    Product[] getAllProducts();

    void saveProduct(Product product);
    void deleteProduct(Product product);
    void deleteAllProducts();
    void updateProduct(Product product);

    // Lifecycle-Methoden (dürfen nicht verändert werden, siehe Spezifikation im Kommentar)

    // Öffnen einer Session (hier sollte die Verbindung zur Datenbank hergestellt werden)
    void openSession();

    // Schließen einer Session (hier sollte die Verbindung zur Datenbank geschlossen werden)
    void closeSession();
}
