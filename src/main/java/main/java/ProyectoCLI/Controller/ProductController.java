package ProyectoCLI.Controller;

import ProyectoCLI.Model.Product;
import ProyectoCLI.Repository.ProductRepository;
import java.util.List;
import java.util.Optional;

/**
 * Controller for managing product-related operations.
 * It interacts with the {@link ProductRepository} to perform CRUD and other actions.
 */
public class ProductController {

    private final ProductRepository productRepository;

    /**
     * Constructs a ProductController with a new instance of {@link ProductRepository}.
     */
    public ProductController() {
        this.productRepository = new ProductRepository();
    }

    /**
     * Adds a new product to the system.
     *
     * @param identifier The unique identifier for the product.
     * @param productName The name of the product.
     * @param productCost The cost of the product.
     * @return The newly created {@link Product} object.
     * @throws IllegalArgumentException if product details are invalid as per Product model validation.
     */
    public Product addProduct(String identifier, String productName, double productCost) {
        try {
            Product newProduct = new Product(identifier, productName, productCost);
            return productRepository.save(newProduct);
        } catch (IllegalArgumentException e) {
            System.err.println("Error al agregar el producto: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Deletes a product by its identifier.
     * Renamed from deleteProductController for consistency.
     *
     * @param identifier The identifier of the product to delete.
     * @return {@code true} if the product was found and deleted, {@code false} otherwise.
     */
    public boolean deleteProductByIdentifier(String identifier) {
        if (identifier == null || identifier.trim().isEmpty()) {
            System.err.println("No se pudo eliminar el producto, identificador vacio o nulo.");
            return false;
        }
        Optional<Product> productOptional = productRepository.findById(identifier);
        if (productOptional.isPresent()) {
            productRepository.deleteById(identifier);
            return true;
        }
        System.err.println("Producto con identificador " + identifier + " no encontrado.");
        return false;
    }

    /**
     * Retrieves a product by its identifier.
     *
     * @param identifier The identifier of the product to find.
     * @return An {@link Optional} containing the product if found, or {@link Optional#empty()} otherwise.
     */
    public Optional<Product> getProductByIdentifier(String identifier) {
        if (identifier == null || identifier.trim().isEmpty()) {
            System.err.println("No se pudo encontrar el producto, identificador vacio o nulo.");
            return Optional.empty();
        }
        return productRepository.findById(identifier);
    }

    /**
     * Edits the name of an existing product.
     *
     * @param identifier The identifier of the product to edit.
     * @param newProductName The new name for the product.
     * @return An {@link Optional} containing the updated product if successful, or {@link Optional#empty()} if the product was not found or the name was invalid.
     */
    public Optional<Product> editProductName(String identifier, String newProductName) {
        if (identifier == null || identifier.trim().isEmpty() || newProductName == null || newProductName.trim().isEmpty()) {
            System.err.println("Entrada invalida para editar el nombre del producto.");
            return Optional.empty();
        }
        try {

            Optional<Product> productOptional = productRepository.findById(identifier);
            if (productOptional.isPresent()) {
                Product productToUpdate = productOptional.get();
                productToUpdate.setProductName(newProductName);
                productRepository.update(productToUpdate);
                return Optional.of(productToUpdate);
            } else {
                System.err.println("Producto con identificador " + identifier + " no encontrado para editar el nombre del producto.");
                return Optional.empty();
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error al editar el nombre del producto: " + e.getMessage());
            return Optional.empty();
        } catch (ProductRepository.ProductNotFoundException e) {
            System.err.println(e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Edits the cost of an existing product.
     *
     * @param identifier The identifier of the product to edit.
     * @param newProductCost The new cost for the product.
     * @return An {@link Optional} containing the updated product if successful, or {@link Optional#empty()} if the product was not found or the cost was invalid.
     */
    public Optional<Product> editProductCost(String identifier, double newProductCost) {
        if (identifier == null || identifier.trim().isEmpty()) {
            System.err.println("Identificador invalido para editar el costo del producto.");
            return Optional.empty();
        }
        try {
            Optional<Product> productOptional = productRepository.findById(identifier);
            if (productOptional.isPresent()) {
                Product productToUpdate = productOptional.get();
                productToUpdate.setProductCost(newProductCost); // Validation in setter
                productRepository.update(productToUpdate);
                return Optional.of(productToUpdate);
            } else {
                System.err.println("Producto con identificador" + identifier + " no encontrado para editar el costo.");
                return Optional.empty();
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error al editar el costo del producto: " + e.getMessage());
            return Optional.empty();
        } catch (ProductRepository.ProductNotFoundException e) {
            System.err.println(e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Retrieves a list of all products.
     *
     * @return A list of all {@link Product} objects.
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Displays a list of all products to the console.
     * @deprecated Prefer returning data to be handled by a dedicated view.
     */
    @Deprecated
    public void displayProductList() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            System.out.println("No hay productos disponibles en el sistema.");
            return;
        }
        System.out.println("\n--- Productos disponibles ---");
        products.forEach(product -> System.out.println(product.getProductData()));
        System.out.println("--------------------------\n");
    }
}

