package ProyectoCLI.CLI;

import ProyectoCLI.Controller.ClientController;
import ProyectoCLI.Model.Client; // Import the model
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Command Line Interface for managing Clients.
 * Allows creating, editing, deleting, and listing clients.
 */
public class ClientMenu {
    private final Scanner scanner;
    private final ClientController clientController;
    private final Confirm confirmUtil; // Renamed for clarity

    /**
     * Constructs a ClientMenu.
     * @param scanner The scanner instance for user input.
     */
    public ClientMenu(Scanner scanner) {
        this.scanner = scanner;
        this.clientController = new ClientController(); // Controller initialized here
        this.confirmUtil = new Confirm(scanner); // Pass scanner to Confirm
    }

    /**
     * Starts the client management menu loop.
     */
    public void startClientMenu() {
        int selection;
        while (true) {
            System.out.println("\n---------------------------------------");
            System.out.println("          GESTION DE CLIENTES        ");
            System.out.println("---------------------------------------");
            System.out.println(" [ 1 ] - Crear Cliente               ");
            System.out.println(" [ 2 ] - Editar Cliente              ");
            System.out.println(" [ 3 ] - Eliminar Cliente            ");
            System.out.println(" [ 4 ] - Listar Todos los Clientes   ");
            System.out.println(" [ 5 ] - Buscar Cliente por CI       ");
            System.out.println(" [ 6 ] - Volver al Menu Principal    ");
            System.out.println("---------------------------------------");
            System.out.print("Seleccione una opcion: ");

            try {
                selection = Integer.parseInt(scanner.nextLine());

                switch (selection) {
                    case 1:
                        createClient();
                        break;
                    case 2:
                        editClient();
                        break;
                    case 3:
                        deleteClient();
                        break;
                    case 4:
                        listAllClients();
                        break;
                    case 5:
                        findClientByCI();
                        break;
                    case 6:
                        return;
                    default:
                        System.out.println("Opcion no valida. Por favor, intente de nuevo.");
                }
                if (selection >= 1 && selection <= 5) {}
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Por favor ingrese un numero.");
            }
        }
    }

    /**
     * Handles the creation of a new client.
     * Prompts the user for client details and calls the controller.
     */
    private void createClient() {
        System.out.println("\n--- Crear Nuevo Cliente ---");
        String nameClient;
        String numCI;
        String emailClient;

        while (true) {
            System.out.print("Ingrese el nombre del cliente: ");
            nameClient = scanner.nextLine().trim();
            if (!nameClient.isEmpty()) break;
            System.out.println("El nombre no puede estar vacio.");
        }

        while (true) {
            System.out.print("Ingrese el numero de CI del cliente: ");
            numCI = scanner.nextLine().trim();
            if (!numCI.isEmpty()) break;
            System.out.println("El numero de CI no puede estar vacio.");
        }

        while (true) {
            System.out.print("Ingrese el email del cliente: ");
            emailClient = scanner.nextLine().trim();
            if (!emailClient.isEmpty()) break;
            System.out.println("El email no puede estar vacio.");
        }

        try {
            Client newClient = clientController.addClient(nameClient, numCI, emailClient);
            System.out.println("Cliente '" + newClient.getName() + "' agregado exitosamente con CI: " + newClient.getNumCI());
        } catch (IllegalArgumentException e) {
            System.err.println("Error al crear cliente: " + e.getMessage());
        }
    }

    /**
     * Handles deleting a client by their CI.
     */
    private void deleteClient() {
        System.out.println("\n--- Eliminar Cliente ---");
        System.out.print("Ingrese el numero de CI del cliente a eliminar: ");
        String numCI = scanner.nextLine().trim();

        if (numCI.isEmpty()) {
            System.out.println("El numero de CI no puede estar vacio.");
            return;
        }

        Optional<Client> clientOptional = clientController.getClientByNumCI(numCI);
        if (clientOptional.isPresent()) {
            Client clientToDelete = clientOptional.get();
            System.out.println("Cliente encontrado: " + clientToDelete.getClientData());
            if (confirmUtil.getYesNoConfirmation("Esta seguro que desea eliminar este cliente")) {
                if (clientController.deleteClientByNumCI(numCI)) {
                    System.out.println("Cliente eliminado exitosamente.");
                } else {
                    System.err.println("Error al eliminar el cliente. Es posible que ya no exista.");
                }
            } else {
                System.out.println("Eliminacion cancelada.");
            }
        } else {
            System.out.println("Cliente con CI '" + numCI + "' no encontrado.");
        }
    }

    /**
     * Handles editing an existing client's details.
     */
    private void editClient() {
        System.out.println("\n--- Editar Cliente ---");
        System.out.print("Ingrese el numero de CI del cliente a editar: ");
        String numCI = scanner.nextLine().trim();

        if (numCI.isEmpty()) {
            System.out.println("El numero de CI no puede estar vacio.");
            return;
        }

        Optional<Client> clientOptional = clientController.getClientByNumCI(numCI);
        if (!clientOptional.isPresent()) {
            System.out.println("Cliente con CI '" + numCI + "' no encontrado.");
            return;
        }

        Client clientToEdit = clientOptional.get();
        System.out.println("Editando cliente: " + clientToEdit.getClientData());

        System.out.println("¿Que desea editar?");
        System.out.println("[ 1 ] - Nombre (Actual: " + clientToEdit.getName() + ")");
        System.out.println("[ 2 ] - Email (Actual: " + clientToEdit.getEmail() + ")");
        System.out.println("[ 3 ] - Cancelar");
        System.out.print("Seleccione una opcion: ");

        try {
            int editChoice = Integer.parseInt(scanner.nextLine());
            switch (editChoice) {
                case 1:
                    System.out.print("Ingrese el nuevo nombre: ");
                    String newName = scanner.nextLine().trim();
                    if (!newName.isEmpty()) {
                        Optional<Client> updatedClient = clientController.editClientName(numCI, newName);
                        if (updatedClient.isPresent()) {
                            System.out.println("Nombre del cliente actualizado exitosamente a: " + updatedClient.get().getName());
                        } else {
                            System.err.println("No se pudo actualizar el nombre del cliente."); // Controller should log specific error
                        }
                    } else {
                        System.out.println("El nombre no puede estar vacio. No se realizaron cambios.");
                    }
                    break;
                case 2:
                    System.out.print("Ingrese el nuevo email: ");
                    String newEmail = scanner.nextLine().trim();
                    if (!newEmail.isEmpty()) {
                        Optional<Client> updatedClient = clientController.editClientEmail(numCI, newEmail);
                        if (updatedClient.isPresent()) {
                            System.out.println("Email del cliente actualizado exitosamente a: " + updatedClient.get().getEmail());
                        } else {
                            System.err.println("No se pudo actualizar el email del cliente.");
                        }
                    } else {
                        System.out.println("El email no puede estar vacio. No se realizaron cambios.");
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
     * Lists all registered clients to the console.
     */
    private void listAllClients() {
        System.out.println("\n--- Lista de Todos los Clientes ---");
        List<Client> clients = clientController.getAllClients();
        if (clients.isEmpty()) {
            System.out.println("No hay clientes registrados en el sistema.");
        } else {
            clients.forEach(client -> System.out.println(client.getClientData()));
        }
        System.out.println("---------------------------------");
    }

    /**
     * Finds and displays a client by their CI.
     */
    private void findClientByCI() {
        System.out.println("\n--- Buscar Cliente por CI ---");
        System.out.print("Ingrese el numero de CI del cliente a buscar: ");
        String numCI = scanner.nextLine().trim();

        if (numCI.isEmpty()) {
            System.out.println("El numero de CI no puede estar vacio.");
            return;
        }

        Optional<Client> clientOptional = clientController.getClientByNumCI(numCI);
        if (clientOptional.isPresent()) {
            System.out.println("Cliente encontrado:");
            System.out.println(clientOptional.get().getClientData());
        } else {
            System.out.println("Cliente con CI '" + numCI + "' no encontrado.");
        }
        System.out.println("-----------------------------");
    }
}
