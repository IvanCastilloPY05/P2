package ProyectoCLI.CLI;

import ProyectoCLI.Controller.SalesController;
import ProyectoCLI.Model.Sales; // Import the model
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Command Line Interface for managing Sales.
 * Allows creating, deleting, and listing sales.
 */
public class SalesMenu {
    private final Scanner scanner;
    private final SalesController salesController;
    private final Confirm confirmUtil;

    /**
     * Constructs a SalesMenu.
     * @param scanner The scanner instance for user input.
     */
    public SalesMenu(Scanner scanner) {
        this.scanner = scanner;
        this.salesController = new SalesController();
        this.confirmUtil = new Confirm(scanner);
    }

    /**
     * Starts the sales management menu loop.
     */
    public void startSalesMenu() {
        int selection;
        while (true) {
            System.out.println("\n---------------------------------------");
            System.out.println("           GESTION DE VENTAS         ");
            System.out.println("---------------------------------------");
            System.out.println(" [ 1 ] - Registrar Nueva Venta       ");
             System.out.println("║ [ 2 ] - Editar Venta              ");
            System.out.println(" [ 2 ] - Eliminar Venta              ");
            System.out.println(" [ 3 ] - Listar Todas las Ventas     ");
            System.out.println(" [ 4 ] - Buscar Venta por ID         ");
            System.out.println(" [ 5 ] - Listar Ventas por Cliente   ");
            System.out.println(" [ 6 ] - Listar Ventas por Producto  ");
            System.out.println(" [ 7 ] - Volver al Menu Principal    ");
            System.out.println("---------------------------------------");
            System.out.print("Seleccione una opcion: ");

            try {
                selection = Integer.parseInt(scanner.nextLine());

                switch (selection) {
                    case 1:
                        createSale();
                        break;
                    case 2:
                        deleteSale();
                        break;
                    case 3:
                        listAllSales();
                        break;
                    case 4:
                        findSaleById();
                        break;
                    case 5:
                        listSalesByClient();
                        break;
                    case 6:
                        listSalesByProduct();
                        break;
                    case 7:
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
     * Handles the creation of a new sale.
     */
    private void createSale() {
        System.out.println("\n--- Registrar Nueva Venta ---");
        String clientNumCI;
        String productId;
        String saleIdentifier;
        while (true) {
            System.out.print("Ingrese el numero de CI del cliente: ");
            clientNumCI = scanner.nextLine().trim();
            if (!clientNumCI.isEmpty()) break;
            System.out.println("El CI del cliente no puede estar vacio.");
        }

        while (true) {
            System.out.print("Ingrese el ID del producto: ");
            productId = scanner.nextLine().trim();
            if (!productId.isEmpty()) break;
            System.out.println("El ID del producto no puede estar vacio.");
        }

        while (true) {
            System.out.print("Ingrese un identificador unico para esta venta: ");
            saleIdentifier = scanner.nextLine().trim();
            if (!saleIdentifier.isEmpty()) break;
            System.out.println("El identificador de la venta no puede estar vacio.");
        }
        Optional<Sales> newSaleOptional = salesController.addSale(clientNumCI, productId, saleIdentifier);

        if (newSaleOptional.isPresent()) {
            Sales newSale = newSaleOptional.get();
            System.out.println("Venta registrada exitosamente");
            System.out.println(newSale.getSaleData());
        } else {
            System.err.println("No se pudo registrar la venta. Verifique los datos ingresados y que el cliente y producto existan.");
        }
    }

    /**
     * Handles deleting a sale by its ID.
     */
    private void deleteSale() {
        System.out.println("\n--- Eliminar Venta ---");
        System.out.print("Ingrese el ID de la venta a eliminar: ");
        String saleIdentifier = scanner.nextLine().trim();

        if (saleIdentifier.isEmpty()) {
            System.out.println("El ID de la venta no puede estar vacio.");
            return;
        }

        Optional<Sales> saleOptional = salesController.getSaleByIdentifier(saleIdentifier);
        if(saleOptional.isPresent()){
            Sales saleToDelete = saleOptional.get();
            System.out.println("Venta encontrada: " + saleToDelete.getSaleData());
            if (confirmUtil.getYesNoConfirmation("Esta seguro que desea eliminar esta venta")) {
                if (salesController.deleteSaleByIdentifier(saleIdentifier)) {
                    System.out.println("Venta eliminada exitosamente.");
                } else {
                    System.err.println("Error al eliminar la venta.");
                }
            } else {
                System.out.println("Eliminacion cancelada.");
            }
        } else {
            System.out.println("Venta con ID '" + saleIdentifier + "' no encontrada.");
        }
    }

    /**
     * Placeholder for editing a sale. Editing sales can be complex (e.g., affecting inventory, payments).
     * For now, it's not implemented.
     */
    private void editSale() {
        System.out.println("\n--- Editar Venta ---");
        System.out.println("La funcionalidad de editar ventas aun no esta implementada.");
    }

    /**
     * Lists all registered sales.
     */
    private void listAllSales() {
        System.out.println("\n--- Lista de Todas las Ventas ---");
        List<Sales> sales = salesController.getAllSales();
        if (sales.isEmpty()) {
            System.out.println("No hay ventas registradas en el sistema.");
        } else {
            sales.forEach(sale -> System.out.println(sale.getSaleData()));
        }
        System.out.println("---------------------------------");
    }

    /**
     * Finds and displays a sale by its ID.
     */
    private void findSaleById() {
        System.out.println("\n--- Buscar Venta por ID ---");
        System.out.print("Ingrese el ID de la venta a buscar: ");
        String saleIdentifier = scanner.nextLine().trim();

        if (saleIdentifier.isEmpty()) {
            System.out.println("El ID de la venta no puede estar vacio.");
            return;
        }

        Optional<Sales> saleOptional = salesController.getSaleByIdentifier(saleIdentifier);
        if (saleOptional.isPresent()) {
            System.out.println("Venta encontrada:");
            System.out.println(saleOptional.get().getSaleData());
        } else {
            System.out.println("Venta con ID '" + saleIdentifier + "' no encontrada.");
        }
        System.out.println("---------------------------");
    }

    /**
     * Lists all sales for a specific client.
     */
    private void listSalesByClient() {
        System.out.println("\n--- Listar Ventas por Cliente ---");
        System.out.print("Ingrese el numero de CI del cliente: ");
        String clientNumCI = scanner.nextLine().trim();

        if (clientNumCI.isEmpty()) {
            System.out.println("El CI del cliente no puede estar vacio.");
            return;
        }

        List<Sales> clientSales = salesController.getSalesByClient(clientNumCI);
        if (clientSales.isEmpty()) {
            System.out.println("No se encontraron ventas para el cliente con CI: " + clientNumCI);
        } else {
            System.out.println("Ventas para el cliente con CI " + clientNumCI + ":");
            clientSales.forEach(sale -> System.out.println(sale.getSaleData()));
        }
        System.out.println("---------------------------------");
    }

    /**
     * Lists all sales for a specific product.
     */
    private void listSalesByProduct() {
        System.out.println("\n--- Listar Ventas por Producto ---");
        System.out.print("Ingrese el ID del producto: ");
        String productId = scanner.nextLine().trim();

        if (productId.isEmpty()) {
            System.out.println("El ID del producto no puede estar vacio.");
            return;
        }

        List<Sales> productSales = salesController.getSalesByProduct(productId);
        if (productSales.isEmpty()) {
            System.out.println("No se encontraron ventas para el producto con ID: " + productId);
        } else {
            System.out.println("Ventas para el producto con ID " + productId + ":");
            productSales.forEach(sale -> System.out.println(sale.getSaleData()));
        }
        System.out.println("----------------------------------");
    }
}
