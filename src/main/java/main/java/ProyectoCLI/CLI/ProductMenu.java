package ProyectoCLI.CLI;

import ProyectoCLI.Controller.ProductController;
import ProyectoCLI.Model.Product; // Import the model

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Command Line Interface for managing Products.
 * Allows creating, editing, deleting, and listing products.
 */
public class ProductMenu {
    private final Scanner scanner;
    private final ProductController productController;
    private final Confirm confirmUtil;

    /**
     * Constructs a ProductMenu.
     * @param scanner The scanner instance for user input.
     */
    public ProductMenu(Scanner scanner) {
        this.scanner = scanner;
        this.productController = new ProductController();
        this.confirmUtil = new Confirm(scanner);
    }

    /**
     * Starts the product management menu loop.
     */
    public void startProductMenu() {
        int selection;
        while (true) {
            System.out.println("\n---------------------------------------");
            System.out.println("        GESTION DE PRODUCTOS         ");
            System.out.println("---------------------------------------");
            System.out.println(" [ 1 ] - Crear Producto              ");
            System.out.println(" [ 2 ] - Editar Producto             ");
            System.out.println(" [ 3 ] - Eliminar Producto           ");
            System.out.println(" [ 4 ] - Listar Todos los Productos  ");
            System.out.println(" [ 5 ] - Buscar Producto por ID      ");
            System.out.println(" [ 6 ] - Volver al Menu Principal    ");
            System.out.println("---------------------------------------");
            System.out.print("Seleccione una opcion: ");

            try {
                selection = Integer.parseInt(scanner.nextLine());

                switch (selection) {
                    case 1:
                        createProduct();
                        break;
                    case 2:
                        editProduct();
                        break;
                    case 3:
                        deleteProduct();
                        break;
                    case 4:
                        listAllProducts();
                        break;
                    case 5:
                        findProductById();
                        break;
                    case 6:
                        return;
                    default:
                        System.out.println("Opcion no valida. Por favor, intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Por favor ingrese un numero.");
            }
        }
    }

    /**
     * Handles the creation of a new product.
     */
    private void createProduct() {
        System.out.println("\n--- Crear Nuevo Producto ---");
        String identifier;
        String productName;
        double costProduct = 0.0;

        while (true) {
            System.out.print("Ingrese el identificador (ID) del producto: ");
            identifier = scanner.nextLine().trim();
            if (!identifier.isEmpty()) break;
            System.out.println("El identificador no puede estar vacio.");
        }

        while (true) {
            System.out.print("Ingrese el nombre del producto: ");
            productName = scanner.nextLine().trim();
            if (!productName.isEmpty()) break;
            System.out.println("El nombre del producto no puede estar vacio.");
        }

        while (true) {
            System.out.print("Ingrese el costo del producto: ");
            try {
                costProduct = Double.parseDouble(scanner.nextLine());
                if (costProduct >= 0) {
                    break;
                } else {
                    System.out.println("El costo no puede ser negativo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Costo invalido. Por favor ingrese un numero (ej. 25.99).");
            }
        }

        try {
            Product newProduct = productController.addProduct(identifier, productName, costProduct);
            System.out.println("Producto '" + newProduct.getProductName() + "' agregado exitosamente con ID: " + newProduct.getIdentifier());
        } catch (IllegalArgumentException e) {
            System.err.println("Error al crear producto: " + e.getMessage());
        }
    }

    /**
     * Handles deleting a product by its ID.
     */
    private void deleteProduct() {
        System.out.println("\n--- Eliminar Producto ---");
        System.out.print("Ingrese el ID del producto a eliminar: ");
        String identifier = scanner.nextLine().trim();

        if (identifier.isEmpty()) {
            System.out.println("El ID del producto no puede estar vacio.");
            return;
        }

        Optional<Product> productOptional = productController.getProductByIdentifier(identifier);
        if(productOptional.isPresent()){
            Product productToDelete = productOptional.get();
            System.out.println("Producto encontrado: " + productToDelete.getProductData());
            if (confirmUtil.getYesNoConfirmation("Esta seguro que desea eliminar este producto")) {
                if (productController.deleteProductByIdentifier(identifier)) {
                    System.out.println("Producto eliminado exitosamente.");
                } else {
                    System.err.println("Error al eliminar el producto.");
                }
            } else {
                System.out.println("Eliminacion cancelada.");
            }
        } else {
            System.out.println("Producto con ID '" + identifier + "' no encontrado.");
        }
    }

    /**
     * Handles editing an existing product's details.
     */
    private void editProduct() {
        System.out.println("\n--- Editar Producto ---");
        System.out.print("Ingrese el ID del producto a editar: ");
        String identifier = scanner.nextLine().trim();

        if (identifier.isEmpty()) {
            System.out.println("El ID del producto no puede estar vacio.");
            return;
        }

        Optional<Product> productOptional = productController.getProductByIdentifier(identifier);
        if (!productOptional.isPresent()) {
            System.out.println("Producto con ID '" + identifier + "' no encontrado.");
            return;
        }

        Product productToEdit = productOptional.get();
        System.out.println("Editando producto: " + productToEdit.getProductData());

        System.out.println("¿Que desea editar?");
        System.out.println("[ 1 ] - Nombre (Actual: " + productToEdit.getProductName() + ")");
        System.out.println("[ 2 ] - Costo (Actual: " + String.format("%.2f", productToEdit.getProductCost()) + ")");
        System.out.println("[ 3 ] - Cancelar");
        System.out.print("Seleccione una opcion: ");

        try {
            int editChoice = Integer.parseInt(scanner.nextLine());
            switch (editChoice) {
                case 1:
                    System.out.print("Ingrese el nuevo nombre del producto: ");
                    String newName = scanner.nextLine().trim();
                    if (!newName.isEmpty()) {
                        Optional<Product> updatedProduct = productController.editProductName(identifier, newName);
                        if(updatedProduct.isPresent()){
                            System.out.println("Nombre del producto actualizado a: " + updatedProduct.get().getProductName());
                        } else {
                            System.err.println("No se pudo actualizar el nombre del producto.");
                        }
                    } else {
                        System.out.println("El nombre no puede estar vacio. No se realizaron cambios.");
                    }
                    break;
                case 2:
                    System.out.print("Ingrese el nuevo costo del producto: ");
                    try {
                        double newCost = Double.parseDouble(scanner.nextLine());
                        Optional<Product> updatedProduct = productController.editProductCost(identifier, newCost);
                        if(updatedProduct.isPresent()){
                            System.out.println("Costo del producto actualizado a: " + String.format("%.2f", updatedProduct.get().getProductCost()));
                        } else {
                            System.err.println("No se pudo actualizar el costo del producto.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Costo invalido. No se realizaron cambios.");
                    }
                    break;
                case 3:
                    System.out.println("Edicion cancelada.");
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada invalida. Por favor ingrese un numero.");
        }
    }

    /**
     * Lists all registered products to the console.
     */
    private void listAllProducts() {
        System.out.println("\n--- Lista de Todos los Productos ---");
        List<Product> products = productController.getAllProducts();
        if (products.isEmpty()) {
            System.out.println("No hay productos registrados en el sistema.");
        } else {
            products.forEach(product -> System.out.println(product.getProductData()));
        }
        System.out.println("----------------------------------");
    }

    /**
     * Finds and displays a product by its ID.
     */
    private void findProductById() {
        System.out.println("\n--- Buscar Producto por ID ---");
        System.out.print("Ingrese el ID del producto a buscar: ");
        String identifier = scanner.nextLine().trim();
        if (identifier.isEmpty()) {
            System.out.println("El ID del producto no puede estar vacio.");
            return;
        }

        Optional<Product> productOptional = productController.getProductByIdentifier(identifier);
        if (productOptional.isPresent()) {
            System.out.println("Producto encontrado:");
            System.out.println(productOptional.get().getProductData());
        } else {
            System.out.println("Producto con ID '" + identifier + "' no encontrado.");
        }
        System.out.println("----------------------------");
    }
}
