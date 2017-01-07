package agrechnev.service;

import agrechnev.dto.PostDto;
import agrechnev.helpers.Util;
import agrechnev.model.CategoryEntity;
import agrechnev.model.PostEntity;
import agrechnev.model.UserEntity;
import agrechnev.repo.CategoryEntityRepository;
import agrechnev.repo.PostEntityRepository;
import agrechnev.repo.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Oleksiy Grechnyev on 12/29/2016.
 */
@Service
public class PostService extends AbstractService<PostDto, PostEntity> {
    private UserEntityRepository userEntityRepository;
    private CategoryEntityRepository categoryEntityRepository;

    // Constructor, needs user and category repos also
    @Autowired
    public PostService(PostEntityRepository postEntityRepository, UserEntityRepository userEntityRepository,
                       CategoryEntityRepository categoryEntityRepository) {
        this.entityRepo = postEntityRepository;
        this.userEntityRepository = userEntityRepository;
        this.categoryEntityRepository = categoryEntityRepository;
    }

    /**
     * Check if the Dto is valid (disregarding ID)
     * E.g. check if there are no null or empty fields
     *
     * @param dto
     * @return
     */
    @Override
    public boolean isValid(PostDto dto) {
        // Check for non-empty fields
        return !Util.isEmptyString(dto.getTitle()) &&
                !Util.isEmptyString(dto.getText()) &&
                dto.getTimeStamp() != null &&
                //Check for valid user ID
                userEntityRepository.exists(dto.getUserId());
    }

    /**
     * Convert dto to entity (I chose to do things by hand, not with dozer or something)
     *
     * @param dto
     * @return
     */
    @Override
    @Transactional
    protected PostEntity Dto2Entity(PostDto dto) {
        // Create entity
        PostEntity entity = new PostEntity(dto.getTitle(), dto.getText(), dto.getTimeStamp(), dto.getRating());

        // Set id
        entity.setId(dto.getId());

        // Set link to the user
        entity.setUser(userEntityRepository.getOne(dto.getUserId()));

        // Set categories
        for (Long id : dto.getCategories()) {
            entity.getCategories().add(categoryEntityRepository.getOne(id));
        }

        return entity;
    }

    /**
     * Convert Entity to Dto
     *
     * @param entity
     * @return
     */
    @Override
    @Transactional
    protected PostDto Entity2Dto(PostEntity entity) {
        // Create DTO
        PostDto dto = new PostDto(entity.getTitle(), entity.getText(), entity.getTimeStamp(), entity.getRating());
        //Set id
        dto.setId(entity.getId());

        //Set user id
        UserEntity user = entity.getUser();
        dto.setUserId(user.getId());

        // Categories: object -> id
        for (CategoryEntity c : entity.getCategories()) {
            dto.getCategories().add(c.getId());
        }

        // Set user login
        dto.setUserLogin(user.getLogin());

        return dto;
    }

    /**
     * Update entity with a Dto leaving all other fields (id, links) untouched
     *
     * @param entity
     * @param dto
     */
    @Override
    @Transactional
    protected void updateEntity(PostEntity entity, PostDto dto) {
        entity.setTitle(dto.getTitle());
        entity.setText(dto.getText());
        entity.setTimeStamp(dto.getTimeStamp());
        entity.setRating(dto.getRating());

        // Set categories
        entity.getCategories().clear();
        for (Long id : dto.getCategories()) {
            entity.getCategories().add(categoryEntityRepository.getOne(id));
        }
    }
}
