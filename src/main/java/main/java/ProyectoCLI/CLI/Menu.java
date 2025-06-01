package ProyectoCLI.CLI;

import java.util.Scanner;

/**
 * Main menu for the Command Line Interface (CLI) application.
 * It allows navigation to different management sections (Products, Clients, Sales).
 */
public class Menu {

    private final Scanner scanner;
    private final ProductMenu productMenu;
    private final ClientMenu clientMenu;
    private final SalesMenu salesMenu;

    /**
     * Constructs the main Menu.
     * Initializes sub-menus and the scanner.
     * @param scanner The scanner instance to be used throughout the CLI.
     */
    public Menu(Scanner scanner) {
        this.scanner = scanner;
        this.productMenu = new ProductMenu(scanner);
        this.clientMenu = new ClientMenu(scanner);
        this.salesMenu = new SalesMenu(scanner);
    }

    /**
     * Starts the main menu loop, allowing the user to navigate the application.
     */
    public void start() {
        int selection;
        while (true) {
            System.out.println("\n-------------------------------------");
            System.out.println("           MENU PRINCIPAL            ");
            System.out.println("---------------------------------------");
            System.out.println(" [ 1 ] - Gestion de Productos        ");
            System.out.println(" [ 2 ] - Gestion de Clientes         ");
            System.out.println(" [ 3 ] - Gestion de Ventas           ");
            System.out.println(" [ 4 ] - Salir del Programa          ");
            System.out.println("---------------------------------------");
            System.out.print("Seleccione una opcion: ");

            try {
                selection = Integer.parseInt(scanner.nextLine());

                switch (selection) {
                    case 1:
                        productMenu.startProductMenu();
                        break;
                    case 2:
                        clientMenu.startClientMenu();
                        break;
                    case 3:
                        salesMenu.startSalesMenu();
                        break;
                    case 4:
                        System.out.println("Saliendo del programa. ¡Hasta luego!");
                        System.exit(0);
                        return;
                    default:
                        System.out.println("Opcion no valida. Por favor, intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Por favor ingrese un numero.");
            }
        }
    }
}

