package ProyectoCLI.Model;

import java.util.Objects;

/**
 * Represents a Product in the system.
 * Each product has a unique identifier, a name, and a cost.
 */
public class Product {
    private String identifier;
    private String productName;
    private double cost;

    /**
     * Default constructor. Initializes fields to default empty/zero values.
     * Consider if an identifier should be auto-generated or always provided.
     */
    public Product() {
        this.identifier = "";
        this.productName = "";
        this.cost = 0.0;
    }

    /**
     * Constructs a new Product with specified details.
     *
     * @param identifier The unique identifier for the product. Must not be null or empty.
     * @param productName The name of the product. Must not be null or empty.
     * @param cost The cost of the product. Must be non-negative.
     * @throws IllegalArgumentException if any of the validation rules for parameters are violated.
     */
    public Product(String identifier, String productName, double cost) {
        setIdentifier(identifier);
        setProductName(productName);
        setProductCost(cost);
    }

    // Getters

    /**
     * Gets the product's identifier.
     * @return The unique identifier of the product.
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Gets the product's name.
     * @return The name of the product.
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Gets the product's cost.
     * @return The cost of the product.
     */
    public double getProductCost() {
        return cost;
    }

    // Setters

    /**
     * Sets the product's identifier.
     *
     * @param identifier The new unique identifier for the product. Must not be null or empty.
     * @throws IllegalArgumentException if the identifier is null or empty.
     */
    public void setIdentifier(String identifier) {
        validateNotEmpty(identifier, "Identificador del producto");
        this.identifier = identifier.trim();
    }

    /**
     * Sets the product's name.
     * Renamed from setNameProduct for consistency.
     *
     * @param productName The new name for the product. Must not be null or empty.
     * @throws IllegalArgumentException if the productName is null or empty.
     */
    public void setProductName(String productName) {
        validateNotEmpty(productName, "Nombre del producto");
        this.productName = productName.trim();
    }

    /**
     * Sets the product's cost.
     *
     * @param cost The new cost for the product. Must be non-negative.
     * @throws IllegalArgumentException if the cost is negative.
     */
    public void setProductCost(double cost) {
        if (cost < 0) {
            throw new IllegalArgumentException("El coste del producto no puede ser negativo: " + cost);
        }
        this.cost = cost;
    }

    // Validation
    /**
     * Validates if a given string is null or empty after trimming.
     *
     * @param text The string to validate.
     * @param fieldName The name of the field being validated (for clear error messages).
     * @throws IllegalArgumentException if the text is null or empty.
     */
    private void validateNotEmpty(String text, String fieldName) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " no puede estar vacio o nulo.");
        }
    }


    //  Utility Methods

    /**
     * Returns a string representation of the product's data.
     *
     * @return A string containing the product's ID, name, and cost.
     */
    public String getProductData() {
        return String.format("Identificador: %s, Nombre del producto: %s, Coste: %.2f", identifier, productName, cost);
    }

    /**
     * Returns a string representation of the Product object.
     *
     * @return A string representation of the object.
     */
    @Override
    public String toString() {
        return getProductData();
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * Equality is based on the product identifier.
     *
     * @param o The reference object with which to compare.
     * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(identifier, product.identifier);
    }

    /**
     * Returns a hash code value for the object.
     * This method is supported for the benefit of hash tables such as those provided by {@link java.util.HashMap}.
     * The hash code is based on the product identifier.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }
}
