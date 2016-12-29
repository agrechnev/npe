package agrechnev.service;

import agrechnev.dto.Dto;
import agrechnev.model.EntityWithId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by Oleksiy Grechnyev on 12/17/2016.
 * A template service class
 * Derived classes might implement additional methods
 * I could have used something standard like CrudRepository
 * But defining my own abstract class avoids confusion
 * D = Dto class
 * E = respective entity class
 */
public abstract class AbstractService<D extends Dto, E extends EntityWithId> {

    // Entity repo, should be set by the derived class constructor
    protected JpaRepository<E, Long> entityRepo;

    // -------------- Non-abstract methods ------------------

    /**
     * Create a new Dto
     *
     * @param dto
     * @return id
     */
    @Transactional
    public Long create(D dto) {
        // Check if the dto is valid
        if (!isValid(dto)) {
            throw new ServiceException(this.getClass() + ".create() : invalid Dto object");
        }

        // Check if the dto is new (e.g. username does not exist in the database)
        if (!isNew(dto)) {
            throw new ServiceException(this.getClass() + ".create() : Dto object is not allowed");
        }

        //Convert Dto to entity
        E entity = Dto2Entity(dto);

        // Persist the entity, any Sping exceptions will go up the stack
        entityRepo.save(entity);
        entityRepo.flush();
        // Take care of the id
        Long id = entity.getId();
        dto.setId(id);
        return id;
    }

    ;

    /**
     * Update a Dto, uses id from Dto
     * Might skip some fields, like UserDto.password
     *
     * @param dto
     */
    @Transactional
    public void update(D dto) {
        // Check if the dto is valid
        if (!isValid(dto)) {
            throw new ServiceException(this.getClass() + ".update() : invalid Dto object");
        }

        // Check if the dto's ID is valid
        Long id = dto.getId();
        if (id == null || !entityRepo.exists(id)) {
            throw new ServiceException(this.getClass() + ".update() : invalid ID");
        }

        // Get the entity
        E entity = entityRepo.getOne(id);
        // Update relevant fields only, leaving id, links untouched
        updateEntity(entity, dto);

        // Save it (probably not needed, but wouldn't hurt)
        entityRepo.save(entity);
        entityRepo.flush();
    }

    ;

    /**
     * Delete a Dto by id
     *
     * @param id
     */
    @Transactional
    public void delete(Long id) {
        entityRepo.delete(id);
    }

    ;

    /**
     * Get a Dto by id
     *
     * @param id
     * @return
     */
    @Transactional
    public D get(Long id) {
        return Entity2Dto(entityRepo.getOne(id));
    }

    ;

    /**
     * Get all Dtos
     *
     * @return
     */
    @Transactional
    public List<D> getAll() {
        return entityRepo.findAll().stream().map(this::Entity2Dto).collect(Collectors.toList());

    }

    ;

    /**
     * Check if a Dto with given id exists
     *
     * @param id
     * @return
     */
    public boolean exists(Long id) {
        return entityRepo.exists(id);
    }

    ;

    //--------------- Abstract methods ---------------------

    /**
     * Check if the Dto is valid (disregarding ID)
     * E.g. check if there are no null or empty fields
     *
     * @param dto
     * @return
     */
    public abstract boolean isValid(D dto);

    /**
     * Check if the Dto is new (e.g. user name is not repeated)
     *
     * @param dto
     * @return
     */
    public boolean isNew(D dto) {
        return true;
    }

    ;

    /**
     * Convert dto to entity (I chose to do things by hand, not with dozer or something)
     *
     * @param dto
     * @return
     */
    protected abstract E Dto2Entity(D dto);

    /**
     * Convert Entity to Dto
     *
     * @param entity
     * @return
     */
    protected abstract D Entity2Dto(E entity);

    /**
     * Update entity with a Dto leaving all other fields (id, links) untouched
     *
     * @param entity
     * @param dto
     */
    protected abstract void updateEntity(E entity, D dto);
}
