package org.hbrs.seka.uebung1.api;

import org.hbrs.seka.uebung1.model.Product;

/**
 * Spezifikation des Interfaces ProductManagementInt:
 *
 * 1.
 * Zunächst MUSS ein externer Client (außerhalb der Komponente!) mit der Methode
 * openConnection() eine Session explizit öffnen!" );
 *
 * 2.
 * Methoden zur Suche, Einfügen usw. können beliebig ausgeführt werden.
 *
 * 3.
 * Dann MUSS ein externer Client mit der Methode closeConnection() die Session explizit schließen!
 */

public interface ProductManagementInt {
    // Auswahl von CRUD-Methoden (weitere können hinzugefügt werden)
    public Product[] getProductByName(String name);
    public Product[] getAllProducts();

    public void saveProduct(Product product);
    public void deleteProduct(Product product);
    public void deleteAllProducts();
    public void updateProduct(Product product);

    // Lifecycle-Methoden (dürfen nicht verändert werden, siehe Spezifikation im Kommentar)

    // Öffnen einer Session (hier sollte die Verbindung zur Datenbank hergestellt werden)
    public void openSession();

    // Schließen einer Session (hier sollte die Verbindung zur Datenbank geschlossen werden)
    public void closeSession();
}
