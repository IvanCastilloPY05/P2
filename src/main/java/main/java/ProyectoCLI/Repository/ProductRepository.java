package ProyectoCLI.Repository;

import ProyectoCLI.Model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Repository for managing {@link Product} entities.
 * This implementation uses an in-memory map (ConcurrentHashMap) as a data store.
 */
public class ProductRepository implements Repository<Product, String> {

    private static final Map<String, Product> productStore = new ConcurrentHashMap<>();

    /**
     * Retrieves an unmodifiable view of the list of products.
     * For display or iteration purposes where modification is not intended.
     *
     * @return An unmodifiable list of all products.
     */
    public List<Product> getAllProducts() {
        return Collections.unmodifiableList(new ArrayList<>(productStore.values()));
    }


    /**
     * Saves a given product. If a product with the same identifier already exists,
     * it will be updated. Otherwise, it will be added.
     *
     * @param product The product to save. Must not be {@literal null}.
     * @return The saved product.
     * @throws IllegalArgumentException if the product or its identifier is {@literal null}.
     */
    @Override
    public Product save(Product product) {
        Objects.requireNonNull(product, "Para guardar el producto este no debe ser nulo.");
        Objects.requireNonNull(product.getIdentifier(), "El identificador del producto no debe ser nulo");
        productStore.put(product.getIdentifier(), product);
        return product;
    }

    /**
     * Updates an existing product. The product is identified by its identifier.
     *
     * @param product The product entity with updated information. Must not be {@literal null}.
     * @return The updated product.
     * @throws IllegalArgumentException if product or its identifier is {@literal null}.
     * @throws ProductNotFoundException if the product to update is not found.
     */
    @Override
    public Product update(Product product) {
        Objects.requireNonNull(product, "Para actualizar el producto este no debe ser nulo.");
        Objects.requireNonNull(product.getIdentifier(), "Para actualizar el producto su identificador no debe ser nulo.");
        if (!productStore.containsKey(product.getIdentifier())) {
            throw new ProductNotFoundException("No se pudo actualizar el producto " + product.getIdentifier() + " ,no se ha encontrado.");
        }
        productStore.put(product.getIdentifier(), product);
        return product;
    }


    /**
     * Deletes a given product from the store.
     * The product is identified by its object instance (based on its equals method, typically identifier).
     *
     * @param product The product to delete. Must not be {@literal null}.
     * @throws IllegalArgumentException if the product is {@literal null}.
     */
    @Override
    public void delete(Product product) {
        Objects.requireNonNull(product, "Para eliminar el producto el identificador no debe ser nulo.");
        Objects.requireNonNull(product.getIdentifier(), "El identificador no debe ser nulo.");
        productStore.remove(product.getIdentifier());
    }

    /**
     * Deletes a product by its identifier.
     *
     * @param identifier The identifier of the product to delete. Must not be {@literal null}.
     * @throws IllegalArgumentException if identifier is {@literal null}.
     */
    @Override
    public void deleteById(String identifier) {
        Objects.requireNonNull(identifier, "El identificador no debe ser nulo para eliminar por identificador.");
        productStore.remove(identifier);
    }

    /**
     * Retrieves a product by its identifier.
     * Renamed from getProductIdentifier for clarity and consistency with findById.
     *
     * @param identifier The identifier of the product to find. Must not be {@literal null}.
     * @return An {@link Optional} containing the product if found, or {@link Optional#empty()} otherwise.
     * @throws IllegalArgumentException if identifier is {@literal null}.
     */
    @Override
    public Optional<Product> findById(String identifier) {
        Objects.requireNonNull(identifier, "Para buscar producto por identificador el identificador no debe ser nulo.");
        return Optional.ofNullable(productStore.get(identifier));
    }

    /**
     * Returns all products currently stored.
     *
     * @return A new list containing all products. Returns an empty list if no products are stored.
     */
    @Override
    public List<Product> findAll() {
        return new ArrayList<>(productStore.values());
    }

    /**
     * Edits the name of an existing product.
     *
     * @param identifier The identifier of the product to edit.
     * @param newProductName The new name for the product.
     * @return The updated product, or {@link Optional#empty()} if the product was not found.
     * @throws IllegalArgumentException if identifier or newProductName is null/empty.
     */
    public Optional<Product> editProductName(String identifier, String newProductName) {
        Objects.requireNonNull(identifier, "El identificador del producto no debe ser nulo.");
        if (newProductName == null || newProductName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nuevo nombre del producto no debe estar vacio o ser nulo.");
        }
        Optional<Product> productOptional = findById(identifier);
        if (productOptional.isPresent()) {
            Product productToUpdate = productOptional.get();
            productToUpdate.setProductName(newProductName); // Assuming Product model has validation in setter
            save(productToUpdate); // Persist the change
            return Optional.of(productToUpdate);
        }
        return Optional.empty();
    }

    /**
     * Edits the cost of an existing product.
     *
     * @param identifier The identifier of the product to edit.
     * @param newCost The new cost for the product.
     * @return The updated product, or {@link Optional#empty()} if the product was not found.
     * @throws IllegalArgumentException if identifier is null or newCost is negative.
     */
    public Optional<Product> editProductCost(String identifier, double newCost) {
        Objects.requireNonNull(identifier, "El identificador del producto no debe ser nulo para editar su costo.");
        if (newCost < 0) {
            throw new IllegalArgumentException("El nuevo coste del producto no debe ser negativo.");
        }
        Optional<Product> productOptional = findById(identifier);
        if (productOptional.isPresent()) {
            Product productToUpdate = productOptional.get();
            productToUpdate.setProductCost(newCost);
            save(productToUpdate);
            return Optional.of(productToUpdate);
        }
        return Optional.empty();
    }


    /**
     * Lists all products to the standard output.
     * @deprecated Prefer returning data to the controller/view for presentation.
     */
    @Deprecated
    public void printProductList() {
        if (productStore.isEmpty()) {
            System.out.println("No hay productos disponibles.");
            return;
        }
        System.out.println("--- Lista de Productos ---");
        for (Product product : productStore.values()) {
            System.out.println(product.getProductData());
        }
        System.out.println("--------------------");
    }
    /**
     * Custom exception for cases where a product is not found.
     */
    public static class ProductNotFoundException extends RuntimeException {
        public ProductNotFoundException(String message) {
            super(message);
        }
    }
}