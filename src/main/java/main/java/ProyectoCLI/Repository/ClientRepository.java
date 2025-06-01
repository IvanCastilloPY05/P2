package ProyectoCLI.Repository;

import ProyectoCLI.Model.Client;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Repository for managing {@link Client} entities.
 * This implementation uses an in-memory list (ArrayList) as a data store.
 * Consider making this a Singleton if only one instance should manage clients.
 */
public class ClientRepository implements Repository<Client, String> {
    private static final Map<String, Client> clientStore = new ConcurrentHashMap<>();

    /**
     * Retrieves an unmodifiable view of the list of clients.
     * This is primarily for display or iteration purposes where modification is not intended.
     * For data manipulation, use the specific repository methods.
     *
     * @return An unmodifiable list of all clients.
     */
    public List<Client> getAllClients() {
        return Collections.unmodifiableList(new ArrayList<>(clientStore.values()));
    }


    /**
     * Saves a given client. If a client with the same numCI already exists,
     * it will be updated. Otherwise, it will be added.
     *
     * @param client The client to save. Must not be {@literal null}.
     * @return The saved client.
     * @throws IllegalArgumentException if the client or its numCI is {@literal null}.
     */
    @Override
    public Client save(Client client) {
        Objects.requireNonNull(client, "El numero de cedula para guardar no debe ser nulo.");
        Objects.requireNonNull(client.getNumCI(), "El numero de cedula no debe ser nulo.");
        clientStore.put(client.getNumCI(), client);
        return client;
    }

    /**
     * Updates an existing client. The client is identified by its numCI.
     * If no client with the given numCI exists, this method might throw an exception
     * or simply not perform an update, depending on desired behavior.
     * This is effectively the same as save if using a Map store.
     *
     * @param client The client entity with updated information. Must not be {@literal null}.
     * @return The updated client.
     * @throws IllegalArgumentException if client or its numCI is {@literal null}.
     * @throws ClientNotFoundException if the client to update is not found.
     */
    @Override
    public Client update(Client client) {
        Objects.requireNonNull(client, "Para actualizar cliente, cliente no puede ser nulo.");
        Objects.requireNonNull(client.getNumCI(), "Para actualizar cliente, numero de cedula no puede ser nulo");
        if (!clientStore.containsKey(client.getNumCI())) {
            throw new ClientNotFoundException("No se pudo actualizar el cliente" + client.getNumCI() + " ,no se ha encontrado.");
        }
        clientStore.put(client.getNumCI(), client);
        return client;
    }

    /**
     * Deletes a given client from the store.
     * The client is identified by its object instance (based on its equals method, typically numCI).
     *
     * @param client The client to delete. Must not be {@literal null}.
     * @throws IllegalArgumentException if the client is {@literal null}.
     */
    @Override
    public void delete(Client client) {
        Objects.requireNonNull(client, "Para eliminar cliente, el cliente no debe ser nulo.");
        Objects.requireNonNull(client.getNumCI(), "El numero de cedula no debe ser nulo.");
        clientStore.remove(client.getNumCI());
    }

    /**
     * Deletes a client by their National Identification Number (numCI).
     *
     * @param numCI The numCI of the client to delete. Must not be {@literal null}.
     * @throws IllegalArgumentException if numCI is {@literal null}.
     */
    @Override
    public void deleteById(String numCI) {
        Objects.requireNonNull(numCI, "El numero de cedula no debe ser nulo para eliminar cliente por cedula.");
        clientStore.remove(numCI);
    }

    /**
     * Retrieves a client by their National Identification Number (numCI).
     * Renamed from getClientIdentifier for clarity and consistency with findById.
     *
     * @param numCI The numCI of the client to find. Must not be {@literal null}.
     * @return An {@link Optional} containing the client if found, or {@link Optional#empty()} otherwise.
     * @throws IllegalArgumentException if numCI is {@literal null}.
     */
    @Override
    public Optional<Client> findById(String numCI) {
        Objects.requireNonNull(numCI, "El numero de cedula no debe ser nulo para su busqueda.");
        return Optional.ofNullable(clientStore.get(numCI));
    }

    /**
     * Returns all clients currently stored.
     *
     * @return A new list containing all clients. Returns an empty list if no clients are stored.
     */
    @Override
    public List<Client> findAll() {
        return new ArrayList<>(clientStore.values());
    }


    /**
     * Lists all clients to the standard output.
     * This method is generally for debugging or simple CLI output and might be removed
     * in a more view-oriented application.
     * @deprecated Prefer returning data to the controller/view for presentation.
     */
    @Deprecated
    public void printClientList() {
        if (clientStore.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }
        System.out.println("--- Lista de clientes ---");
        for (Client client : clientStore.values()) {
            System.out.println(client.getClientData());
        }
        System.out.println("-------------------");
    }

    /**
     * Custom exception for cases where a client is not found.
     */
    public static class ClientNotFoundException extends RuntimeException {
        public ClientNotFoundException(String message) {
            super(message);
        }
    }
}
