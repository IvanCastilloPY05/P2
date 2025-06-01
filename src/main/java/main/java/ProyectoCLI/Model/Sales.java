package ProyectoCLI.Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Represents a Sale transaction in the system.
 * A sale links a {@link Client} and a {@link Product}, and includes a unique sale identifier,
 * the date of purchase, and a status indicating if it's sold.
 */
public class Sales {
    private Client client;
    private Product product;
    private String saleIdentifier;
    private final LocalDate purchaseDate;
    private boolean sold;

    /**
     * Default constructor. Initializes with null client/product and sold status to false.
     * The purchase date is set to the current date.
     * This might be used if a sale object is created incrementally.
     */
    public Sales() {
        this.client = null;
        this.product = null;
        this.saleIdentifier = "";
        this.purchaseDate = LocalDate.now();
        this.sold = false;
    }

    /**
     * Constructs a new Sale with specified client, product, and sale identifier.
     * The purchase date is set to the current date automatically.
     * The 'sold' status defaults to true upon creation of a sale record,
     * assuming a sale record implies a completed transaction.
     *
     * @param client The client making the purchase. Must not be {@literal null}.
     * @param product The product being sold. Must not be {@literal null}.
     * @param saleIdentifier The unique identifier for this sale. Must not be null or empty.
     * @throws IllegalArgumentException if client, product, or saleIdentifier is null or empty.
     */
    public Sales(Client client, Product product, String saleIdentifier) {
        setClient(client);
        setProduct(product);
        setSaleIdentifier(saleIdentifier);
        this.purchaseDate = LocalDate.now();
        this.sold = true;
    }

    // Getters

    /**
     * Gets the client associated with this sale.
     * @return The {@link Client}.
     */
    public Client getClient() {
        return client;
    }

    /**
     * Gets the product associated with this sale.
     * @return The {@link Product}.
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Gets the unique identifier for this sale.
     * @return The sale identifier.
     */
    public String getSaleIdentifier() {
        return saleIdentifier;
    }

    /**
     * Gets the date of purchase.
     * @return The {@link LocalDate} of the purchase.
     */
    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    /**
     * Checks if the sale transaction is marked as completed (sold).
     * @return {@code true} if the product is considered sold in this transaction, {@code false} otherwise.
     */
    public boolean isSold() {
        return sold;
    }

    // Setters

    /**
     * Sets the client for this sale.
     * @param client The client involved in the sale. Must not be {@literal null}.
     * @throws IllegalArgumentException if the client is {@literal null}.
     */
    public void setClient(Client client) {
        Objects.requireNonNull(client, "Un cliente no puede ser nulo para comprar.");
        this.client = client;
    }

    /**
     * Sets the product for this sale.
     * @param product The product involved in the sale. Must not be {@literal null}.
     * @throws IllegalArgumentException if the product is {@literal null}.
     */
    public void setProduct(Product product) {
        Objects.requireNonNull(product, "Un producto no puede ser nulo para venderse.");
        this.product = product;
    }

    /**
     * Sets the unique identifier for this sale.
     * @param saleIdentifier The sale identifier. Must not be null or empty.
     * @throws IllegalArgumentException if the saleIdentifier is null or empty.
     */
    public void setSaleIdentifier(String saleIdentifier) {
        if (saleIdentifier == null || saleIdentifier.trim().isEmpty()) {
            throw new IllegalArgumentException("El identificador no debe ser nulo o vacio");
        }
        this.saleIdentifier = saleIdentifier.trim();
    }

    /**
     * Sets the sold status of this sale.
     * For example, this could be used to mark a sale as pending or cancelled if needed.
     * @param sold The new sold status.
     */
    public void setSold(boolean sold) {
        this.sold = sold;
    }

    // Utility Methods

    /**
     * Returns a string representation of the sale's data.
     *
     * @return A string containing details of the sale including client, product, sale ID, and purchase date.
     */
    public String getSaleData() {
        String clientInfo = (client != null) ? client.getName() : "N/A";
        String productInfo = (product != null) ? product.getProductName() : "N/A";
        return String.format("Identificador venta: %s, Cliente: %s, Producto: %s, Fecha: %s, Estado: %s",
                saleIdentifier,
                clientInfo,
                productInfo,
                purchaseDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
                sold ? "Vendido" : "Pendiente/Sin Vender");
    }

    /**
     * Returns a string representation of the Sales object.
     *
     * @return A string representation of the object.
     */
    @Override
    public String toString() {
        return getSaleData();
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * Equality is based on the saleIdentifier.
     *
     * @param o The reference object with which to compare.
     * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sales sales = (Sales) o;
        return Objects.equals(saleIdentifier, sales.saleIdentifier);
    }

    /**
     * Returns a hash code value for the object.
     * This method is supported for the benefit of hash tables such as those provided by {@link java.util.HashMap}.
     * The hash code is based on the saleIdentifier.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(saleIdentifier);
    }
}
