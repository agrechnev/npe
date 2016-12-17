package agrechnev.service;

import java.util.List;

/**
 * Created by Oleksiy Grechnyev on 12/17/2016.
 * A template service class
 * Some services might implement additional methods
 * I could have used something standard like CrudRepository
 * But defining my own Interface avoids confusion
 */
public interface NpeService<T> {

    /**
     * Create a new Dto
     *
     * @param dto
     * @return id
     */
    Long create(T dto);

    /**
     * Update a Dto, uses id from Dto
     * Might skip some fields, like UserDto.password
     *
     * @param dto
     */
    void update(T dto);

    /**
     * Delete a Dto by id
     *
     * @param id
     */
    void delete(Long id);

    /**
     * Get a Dto by id
     *
     * @param id
     * @return
     */
    T get(Long id);

    /**
     * Get all Dtos
     *
     * @return
     */
    List<T> getAll();

    /**
     * Check if a Dto with given id exists
     *
     * @param id
     * @return
     */
    boolean exists(Long id);
}
