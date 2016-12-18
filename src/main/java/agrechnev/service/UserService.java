package agrechnev.service;

import agrechnev.dto.UserDto;
import agrechnev.helpers.Util;
import agrechnev.model.UserEntity;
import agrechnev.repo.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by Oleksiy Grechnyev on 12/17/2016.
 * UserDto service
 * Throws (undeclared) ServiceException or Spring exceptions if anything goes wrong
 */
@Service
public class UserService extends AbstractService<UserDto, UserEntity> {

    // Constructor
    @Autowired
    public UserService(UserEntityRepository userEntityRepository) {
        this.entityRepo = userEntityRepository;
    }


    /**
     * Check if the Dto is valid (disregarding ID)
     * E.g. check if there are no null strings etc
     *
     * @param dto
     * @return
     */
    public boolean isValid(UserDto dto) {
        if (
                Util.isEmptyString(dto.getLogin()) ||
                        Util.isEmptyString(dto.getPassw()) ||
                        Util.isEmptyString(dto.getFullName()) ||
                        Util.isEmptyString(dto.getEmail()) ||
                        dto.getRole() == null
                ) return false;
        else
            return true;
    }

    //---------------------------------------------------

    /**
     * Convert dto to entity (I chose to do things by hand, not with dozer or something)
     *
     * @param dto
     * @return
     */
    protected UserEntity Dto2Entity(UserDto dto) {
        UserEntity entity = new UserEntity(
                dto.getLogin(), dto.getPassw(), dto.getFullName(), dto.getEmail(), dto.getPoints(), dto.getRole()
        );

        // Not needed for create, needed for update
        entity.setId(dto.getId());

        return entity;
    }

    /**
     * Check if the Dto is new (e.g. user name is not repeated)
     * @param dto
     * @return
     */
    @Override
    public boolean isNew(UserDto dto) {
        // Check if a user with this login already exists
        Optional<UserEntity> o = ((UserEntityRepository) entityRepo).findByLogin(dto.getLogin());
        return !o.isPresent();
    }

    /**
     * Convert Entity to Dto
     * @param entity
     * @return
     */
    @Override
    protected UserDto Entity2Dto(UserEntity entity) {
        UserDto dto = new UserDto(
                entity.getLogin(), entity.getPassw(), entity.getFullName(),
                entity.getEmail(), entity.getPoints(), entity.getRole()
        );

        dto.setId(entity.getId());

        return dto;
    }

    /**
     * Update entity with a Dto leaving all other fields (id, links) untouched
     * @param entity
     * @param dto
     */
    @Override
    protected void updateEntity(UserEntity entity, UserDto dto) {
        entity.setLogin(dto.getLogin());
        entity.setPassw(dto.getPassw());
        entity.setFullName(dto.getFullName());
        entity.setEmail(dto.getEmail());
        entity.setPoints(dto.getPoints());
        entity.setRole(dto.getRole());
    }
}
