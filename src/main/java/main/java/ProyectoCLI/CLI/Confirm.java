package ProyectoCLI.CLI;

import java.util.Scanner;

/**
 * Utility class to handle confirmation prompts after an action in a menu.
 * It allows the user to return to the main menu or exit the application.
 */
public class Confirm {

    private final Scanner scanner;

    /**
     * Constructs a Confirm utility.
     * @param scanner The scanner instance to use for input.
     */
    public Confirm(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Displays a confirmation prompt to the user, allowing them to
     * return to the main menu or exit the program.
     * It expects the main menu logic to be handled by a {@link Menu} class.
     */
    public void confirmAction() {
        int choice = -1;
        while (true) {
            System.out.println("\n--- Action Completed ---");
            System.out.println("[ 1 ] - Volver al menu principal (Main Menu)");
            System.out.println("[ 2 ] - Salir del programa (Exit Program)");
            System.out.print("Seleccione una opcion: ");
            try {
                choice = Integer.parseInt(scanner.nextLine()); // Read as line and parse
                switch (choice) {
                    case 1:
                        return;
                    case 2:
                        System.out.println("Saliendo del programa.");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Opcion no valida. Por favor ingrese 1 o 2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Por favor ingrese un numero.");
            }
        }
    }

    /**
     * Prompts the user for a yes/no confirmation.
     *
     * @param message The message to display for the confirmation.
     * @return {@code true} if the user confirms (yes), {@code false} otherwise (no).
     */
    public boolean getYesNoConfirmation(String message) {
        while (true) {
            System.out.print(message + " (s/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if ("s".equals(input)) {
                return true;
            } else if ("n".equals(input)) {
                return false;
            } else {
                System.out.println("Respuesta invalida. Por favor ingrese 's' para si o 'n' para no.");
            }
        }
    }
}

