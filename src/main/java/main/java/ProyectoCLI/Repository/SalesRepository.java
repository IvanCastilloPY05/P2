package ProyectoCLI.Repository;

import ProyectoCLI.Model.Sales;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Repository for managing {@link Sales} entities.
 * This implementation uses an in-memory map (ConcurrentHashMap) as a data store.
 * The key for the map is the sale's identifier.
 */
public class SalesRepository implements Repository<Sales, String> {

    private static final Map<String, Sales> salesStore = new ConcurrentHashMap<>();

    /**
     * Retrieves an unmodifiable view of the list of sales.
     * For display or iteration purposes where modification is not intended.
     *
     * @return An unmodifiable list of all sales.
     */
    public List<Sales> getAllSales() {
        return Collections.unmodifiableList(new ArrayList<>(salesStore.values()));
    }

    /**
     * Saves a given sale. If a sale with the same identifier already exists,
     * it will be updated. Otherwise, it will be added.
     *
     * @param sale The sale to save. Must not be {@literal null}.
     * @return The saved sale.
     * @throws IllegalArgumentException if the sale or its identifier is {@literal null}.
     */
    @Override
    public Sales save(Sales sale) {
        Objects.requireNonNull(sale, "Para guardar la venta el identificador no debe ser nulo.");
        Objects.requireNonNull(sale.getSaleIdentifier(), "El identificador de la venta no debe ser nulo.");
        salesStore.put(sale.getSaleIdentifier(), sale);
        return sale;
    }

    /**
     * Updates an existing sale. The sale is identified by its saleIdentifier.
     *
     * @param sale The sale entity with updated information. Must not be {@literal null}.
     * @return The updated sale.
     * @throws IllegalArgumentException if sale or its identifier is {@literal null}.
     * @throws SaleNotFoundException if the sale to update is not found.
     */
    @Override
    public Sales update(Sales sale) {
        Objects.requireNonNull(sale, "Sale to update cannot be null.");
        Objects.requireNonNull(sale.getSaleIdentifier(), "El identificador de la venta no debe ser nulo para su actualizacion.");
        if (!salesStore.containsKey(sale.getSaleIdentifier())) {
            throw new SaleNotFoundException("No se pudo actualizar la venta" + sale.getSaleIdentifier() + " ,no fue encontrada.");
        }
        salesStore.put(sale.getSaleIdentifier(), sale);
        return sale;
    }


    /**
     * Deletes a given sale from the store.
     * The sale is identified by its object instance (based on its equals method, typically saleIdentifier).
     *
     * @param sale The sale to delete. Must not be {@literal null}.
     * @throws IllegalArgumentException if the sale is {@literal null}.
     */
    @Override
    public void delete(Sales sale) {
        Objects.requireNonNull(sale, "El identificador de la venta no debe ser nulo");
        Objects.requireNonNull(sale.getSaleIdentifier(), "El identificador de la venta no debe ser nulo para su eliminacion.");
        salesStore.remove(sale.getSaleIdentifier());
    }

    /**
     * Deletes a sale by its identifier.
     *
     * @param saleIdentifier The identifier of the sale to delete. Must not be {@literal null}.
     * @throws IllegalArgumentException if saleIdentifier is {@literal null}.
     */
    @Override
    public void deleteById(String saleIdentifier) {
        Objects.requireNonNull(saleIdentifier, "El identificador de la venta no debe ser nulo para su eliminacion.");
        salesStore.remove(saleIdentifier);
    }

    /**
     * Retrieves a sale by its identifier.
     * Renamed from getSalesIdentifier for clarity and consistency.
     *
     * @param saleIdentifier The identifier of the sale to find. Must not be {@literal null}.
     * @return An {@link Optional} containing the sale if found, or {@link Optional#empty()} otherwise.
     * @throws IllegalArgumentException if saleIdentifier is {@literal null}.
     */
    @Override
    public Optional<Sales> findById(String saleIdentifier) {
        Objects.requireNonNull(saleIdentifier, "El identificador de la venta no debe ser nulo para su busqueda.");
        return Optional.ofNullable(salesStore.get(saleIdentifier));
    }

    /**
     * Returns all sales currently stored.
     *
     * @return A new list containing all sales. Returns an empty list if no sales are stored.
     */
    @Override
    public List<Sales> findAll() {
        return new ArrayList<>(salesStore.values());
    }

    /**
     * Finds all sales associated with a specific client.
     *
     * @param clientId The national identification number (numCI) of the client. Must not be {@literal null}.
     * @return A list of {@link Sales} made by the specified client. Returns an empty list if no sales are found.
     * @throws IllegalArgumentException if clientId is {@literal null}.
     */
    public List<Sales> findSalesByClientId(String clientId) {
        Objects.requireNonNull(clientId, "El numero de documento del cliente no debe ser nulo.");
        return salesStore.values().stream()
                .filter(sale -> sale.getClient() != null && clientId.equals(sale.getClient().getNumCI()))
                .collect(Collectors.toList());
    }

    /**
     * Finds all sales associated with a specific product.
     *
     * @param productId The identifier of the product. Must not be {@literal null}.
     * @return A list of {@link Sales} involving the specified product. Returns an empty list if no sales are found.
     * @throws IllegalArgumentException if productId is {@literal null}.
     */
    public List<Sales> findSalesByProductId(String productId) {
        Objects.requireNonNull(productId, "El identificador del producto no debe ser nulo.");
        return salesStore.values().stream()
                .filter(sale -> sale.getProduct() != null && productId.equals(sale.getProduct().getIdentifier()))
                .collect(Collectors.toList());
    }

    /**
     * Lists all sales to the standard output.
     * @deprecated Prefer returning data to the controller/view for presentation.
     */
    @Deprecated
    public void printSalesList() {
        if (salesStore.isEmpty()) {
            System.out.println("No hay ventas guardadas.");
            return;
        }
        System.out.println("--- Lista de Ventas ---");
        for (Sales sale : salesStore.values()) {
            System.out.println(sale.getSaleData());
        }
        System.out.println("------------------");
    }
    /**
     * Custom exception for cases where a sale is not found.
     */
    public static class SaleNotFoundException extends RuntimeException {
        public SaleNotFoundException(String message) {
            super(message);
        }
    }
}
