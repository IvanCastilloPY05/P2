package ProyectoCLI.Controller;

import ProyectoCLI.Model.Client;
import ProyectoCLI.Repository.ClientRepository;
import java.util.List;
import java.util.Optional;

/**
 * Controller for managing client-related operations.
 * It interacts with the {@link ClientRepository} to perform CRUD and other actions.
 */
public class ClientController {

    private final ClientRepository clientRepository;

    /**
     * Constructs a ClientController with a new instance of {@link ClientRepository}.
     * Consider dependency injection for more flexibility in managing repository instances.
     */
    public ClientController() {
        this.clientRepository = new ClientRepository();
    }

    /**
     * Adds a new client to the system.
     *
     * @param clientName The name of the client.
     * @param numCI The national identification number of the client.
     * @param clientEmail The email address of the client.
     * @return The newly created {@link Client} object.
     * @throws IllegalArgumentException if client details are invalid as per Client model validation.
     */
    public Client addClient(String clientName, String numCI, String clientEmail) {
        try {
            Client newClient = new Client(clientName, numCI, clientEmail);
            return clientRepository.save(newClient);
        } catch (IllegalArgumentException e) {
            System.err.println("Error al agregar el cliente: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Deletes a client by their National Identification Number (numCI).
     * Renamed from deleteProductController for clarity.
     *
     * @param numCI The numCI of the client to delete.
     * @return {@code true} if the client was found and deleted, {@code false} otherwise.
     */
    public boolean deleteClientByNumCI(String numCI) {
        if (numCI == null || numCI.trim().isEmpty()) {
            System.err.println("No se pudo eliminar el cliente porque el numero de cedula esta vacio o es nulo.");
            return false;
        }
        Optional<Client> clientOptional = clientRepository.findById(numCI);
        if (clientOptional.isPresent()) {
            clientRepository.deleteById(numCI);
            return true;
        }
        System.err.println("Cliente con numero de cedula" + numCI + " no encontrado.");
        return false;
    }

    /**
     * Retrieves a client by their National Identification Number (numCI).
     *
     * @param numCI The numCI of the client to find.
     * @return An {@link Optional} containing the client if found, or {@link Optional#empty()} otherwise.
     */
    public Optional<Client> getClientByNumCI(String numCI) {
        if (numCI == null || numCI.trim().isEmpty()) {
            System.err.println("No se pudo encontrar cliente, entrada vacia o nula.");
            return Optional.empty();
        }
        return clientRepository.findById(numCI);
    }

    /**
     * Edits the name of an existing client.
     *
     * @param numCI The numCI of the client to edit.
     * @param newName The new name for the client.
     * @return An {@link Optional} containing the updated client if successful, or {@link Optional#empty()} if the client was not found or the name was invalid.
     */
    public Optional<Client> editClientName(String numCI, String newName) {
        if (numCI == null || numCI.trim().isEmpty() || newName == null || newName.trim().isEmpty()) {
            System.err.println("Entrada invalida para editar nombre del cliente.");
            return Optional.empty();
        }
        try {
            Optional<Client> clientOptional = clientRepository.findById(numCI);
            if (clientOptional.isPresent()) {
                Client clientToUpdate = clientOptional.get();
                clientToUpdate.setName(newName);
                clientRepository.update(clientToUpdate);
                return Optional.of(clientToUpdate);
            } else {
                System.err.println("Cliente con numero de cedula" + numCI + " no registrado para editar el nombre del cliente.");
                return Optional.empty();
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error al editar el nombre del cliente: " + e.getMessage());
            return Optional.empty();
        } catch (ClientRepository.ClientNotFoundException e) {
            System.err.println(e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Edits the email of an existing client.
     *
     * @param numCI The numCI of the client to edit.
     * @param newEmail The new email for the client.
     * @return An {@link Optional} containing the updated client if successful, or {@link Optional#empty()} if the client was not found or the email was invalid.
     */
    public Optional<Client> editClientEmail(String numCI, String newEmail) {
        if (numCI == null || numCI.trim().isEmpty() || newEmail == null || newEmail.trim().isEmpty()) {
            System.err.println("Entrada invalida para editar el cliente.");
            return Optional.empty();
        }
        try {
            Optional<Client> clientOptional = clientRepository.findById(numCI);
            if (clientOptional.isPresent()) {
                Client clientToUpdate = clientOptional.get();
                clientToUpdate.setEmail(newEmail);
                clientRepository.update(clientToUpdate);
                return Optional.of(clientToUpdate);
            } else {
                System.err.println("Cliente con numero de cedula " + numCI + " no encontrado para editar el email.");
                return Optional.empty();
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error al editar el email: " + e.getMessage());
            return Optional.empty();
        } catch (ClientRepository.ClientNotFoundException e) {
            System.err.println(e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Retrieves a list of all clients.
     * The view layer would typically call this to display clients.
     *
     * @return A list of all {@link Client} objects.
     */
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    /**
     * Displays a list of all clients to the console.
     * This method is intended for simple CLI interactions. In a GUI or web application,
     * the data would be passed to a view component.
     * @deprecated Prefer returning data to be handled by a dedicated view.
     */
    @Deprecated
    public void displayClientList() {
        List<Client> clients = clientRepository.findAll();
        if (clients.isEmpty()) {
            System.out.println("No hay clientes registrados en el sistema.");
            return;
        }
        System.out.println("\n--- Cliente registrados ---");
        clients.forEach(client -> System.out.println(client.getClientData()));
        System.out.println("--------------------------\n");
    }
}
