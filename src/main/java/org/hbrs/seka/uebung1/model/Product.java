package org.hbrs.seka.uebung1.model;

/**
 * Klasse zur Repräsentation eines Produkts. Dies kann um weitere Attribute gerne erweitert werden.
 */
public class Product {
    private int id = 0;
    private String name;
    private double price;

    public Product(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }

    /**
     * Vergleich von zwei Produkten anhand des Namens und des Preises.
     * ID wird nicht berücksichtigt, da es in der Datenbank iterativ generiert wird.
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Product) {
            Product p = (Product) o;
            return p.getName().equals(this.getName()) && p.getPrice() == this.getPrice();
        }
        return false;
    }
}
