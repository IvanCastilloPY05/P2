package ProyectoCLI.Controller;

import ProyectoCLI.Model.Client;
import ProyectoCLI.Model.Product;
import ProyectoCLI.Model.Sales;
import ProyectoCLI.Repository.ClientRepository;
import ProyectoCLI.Repository.ProductRepository;
import ProyectoCLI.Repository.SalesRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Controller for managing sales-related operations.
 * It interacts with {@link SalesRepository}, {@link ClientRepository}, and {@link ProductRepository}.
 */
public class SalesController {

    private final SalesRepository salesRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;

    /**
     * Constructs a SalesController with new instances of relevant repositories.
     * Consider dependency injection for managing repository instances.
     */
    public SalesController() {
        this.salesRepository = new SalesRepository();
        this.clientRepository = new ClientRepository();
        this.productRepository = new ProductRepository();
    }

    /**
     * Adds a new sale to the system.
     * It requires valid client and product identifiers to fetch the actual objects.
     *
     * @param clientNumCI The National Identification Number (numCI) of the client.
     * @param productId The identifier of the product.
     * @param saleIdentifier The unique identifier for this sale.
     * @return An {@link Optional} containing the newly created {@link Sales} object if successful,
     * or {@link Optional#empty()} if the client or product is not found or input is invalid.
     * @throws IllegalArgumentException if saleIdentifier is null/empty or if sale creation fails.
     */
    public Optional<Sales> addSale(String clientNumCI, String productId, String saleIdentifier) {
        if (clientNumCI == null || clientNumCI.trim().isEmpty() ||
                productId == null || productId.trim().isEmpty() ||
                saleIdentifier == null || saleIdentifier.trim().isEmpty()) {
            System.err.println("Entrada invalida, identificador producto, numero documento cliente o identificador venta no deben estar vacios o nulos");
            return Optional.empty();
        }

        Optional<Client> clientOptional = clientRepository.findById(clientNumCI);
        if (!clientOptional.isPresent()) {
            System.err.println("No se pudo agregar la venta al cliente " + clientNumCI + " ,cliente no encontrado.");
            return Optional.empty();
        }

        Optional<Product> productOptional = productRepository.findById(productId);
        if (!productOptional.isPresent()) {
            System.err.println("No se pudo agregar la venta al producto " + productId + " ,producto no encontrado.");
            return Optional.empty();
        }

        try {
            Client client = clientOptional.get();
            Product product = productOptional.get();
            Sales newSale = new Sales(client, product, saleIdentifier);
            salesRepository.save(newSale);
            return Optional.of(newSale);
        } catch (IllegalArgumentException e) {
            System.err.println("Error al agregar venta: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Deletes a sale by its identifier.
     * Renamed from deleteProductController for clarity.
     *
     * @param saleIdentifier The identifier of the sale to delete.
     * @return {@code true} if the sale was found and deleted, {@code false} otherwise.
     */
    public boolean deleteSaleByIdentifier(String saleIdentifier) {
        if (saleIdentifier == null || saleIdentifier.trim().isEmpty()) {
            System.err.println("No se pudo eliminar la venta, el identificador es nulo o vacio.");
            return false;
        }
        Optional<Sales> saleOptional = salesRepository.findById(saleIdentifier);
        if (saleOptional.isPresent()) {
            salesRepository.deleteById(saleIdentifier);
            return true;
        }
        System.err.println("Venta con identificador " + saleIdentifier + " no encontrada.");
        return false;
    }

    /**
     * Retrieves a sale by its identifier.
     *
     * @param saleIdentifier The identifier of the sale to find.
     * @return An {@link Optional} containing the sale if found, or {@link Optional#empty()} otherwise.
     */
    public Optional<Sales> getSaleByIdentifier(String saleIdentifier) {
        if (saleIdentifier == null || saleIdentifier.trim().isEmpty()) {
            System.err.println("No se pudo encontrar ventas, identificador nulo o vacio.");
            return Optional.empty();
        }
        return salesRepository.findById(saleIdentifier);
    }

    /**
     * Retrieves all sales recorded in the system.
     *
     * @return A list of all {@link Sales} objects.
     */
    public List<Sales> getAllSales() {
        return salesRepository.findAll();
    }

    /**
     * Retrieves all sales made by a specific client.
     *
     * @param clientNumCI The National Identification Number (numCI) of the client.
     * @return A list of {@link Sales} for the given client, or an empty list if none found or clientNumCI is invalid.
     */
    public List<Sales> getSalesByClient(String clientNumCI) {
        if (clientNumCI == null || clientNumCI.trim().isEmpty()) {
            System.err.println("No se pudo encontrar ventas al cliente, numero de documento vacio o nulo.");
            return Collections.emptyList();
        }
        return salesRepository.findSalesByClientId(clientNumCI);
    }

    /**
     * Retrieves all sales involving a specific product.
     *
     * @param productId The identifier of the product.
     * @return A list of {@link Sales} for the given product, or an empty list if none found or productId is invalid.
     */
    public List<Sales> getSalesByProduct(String productId) {
        if (productId == null || productId.trim().isEmpty()) {
            System.err.println("No se pudo encontrar ventas del producto, identificador del producto vacio o nulo.");
            return Collections.emptyList();
        }
        return salesRepository.findSalesByProductId(productId);
    }

    /**
     * Displays a list of all sales to the console.
     * @deprecated Prefer returning data to be handled by a dedicated view.
     */
    @Deprecated
    public void displaySalesList() {
        List<Sales> sales = salesRepository.findAll();
        if (sales.isEmpty()) {
            System.out.println("No existen ventas guardadas en el sistema.");
            return;
        }
        System.out.println("\n--- Ventas Guardadas ---");
        sales.forEach(sale -> System.out.println(sale.getSaleData()));
        System.out.println("----------------------\n");
    }
}