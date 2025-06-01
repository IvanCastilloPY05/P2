package ProyectoCLI.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Generic Repository interface for CRUD operations.
 *
 * @param <T> The type of the entity.
 * @param <ID> The type of the entity's identifier.
 */
public interface Repository<T, ID> {

    /**
     * Saves a given entity.
     * Use the returned instance for further operations as the save operation might have changed the
     * entity instance completely.
     *
     * @param entity an entity to be saved. Must not be {@literal null}.
     * @return the saved entity; will never be {@literal null}.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    T save(T entity);

    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@link Optional#empty()} if none found.
     * @throws IllegalArgumentException if {@code id} is {@literal null}.
     */
    Optional<T> findById(ID id);

    /**
     * Returns all instances of the type.
     *
     * @return all entities; will never be {@literal null}.
     */
    List<T> findAll();

    /**
     * Deletes a given entity.
     *
     * @param entity an entity to be deleted. Must not be {@literal null}.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    void delete(T entity);

    /**
     * Deletes the entity with the given id.
     *
     * @param id must not be {@literal null}.
     * @throws IllegalArgumentException if {@code id} is {@literal null}.
     */
    void deleteById(ID id);

    /**
     * Updates a given entity.
     * Use the returned instance for further operations as the update operation might have changed the
     * entity instance completely.
     *
     * @param entity an entity to be updated. Must not be {@literal null}.
     * @return the updated entity; will never be {@literal null}.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    T update(T entity);
}