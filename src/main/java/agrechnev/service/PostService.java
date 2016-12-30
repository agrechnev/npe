package agrechnev.service;

import agrechnev.dto.PostDto;
import agrechnev.helpers.Util;
import agrechnev.model.PostEntity;
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

    // Constructor, needs user repo also
    @Autowired
    public PostService(PostEntityRepository postEntityRepository, UserEntityRepository userEntityRepository) {
        this.entityRepo = postEntityRepository;
        this.userEntityRepository = userEntityRepository;
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
    protected PostEntity Dto2Entity(PostDto dto) {
        // Create entity
        PostEntity entity = new PostEntity(dto.getTitle(), dto.getText(), dto.getTimeStamp(), dto.getRating());

        // Set id
        entity.setId(dto.getId());

        // Set link to the user
        entity.setUser(userEntityRepository.getOne(dto.getUserId()));

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
        dto.setUserId(entity.getUser().getId());

        return dto;
    }

    /**
     * Update entity with a Dto leaving all other fields (id, links) untouched
     *
     * @param entity
     * @param dto
     */
    @Override
    protected void updateEntity(PostEntity entity, PostDto dto) {
        entity.setTitle(dto.getTitle());
        entity.setText(dto.getText());
        entity.setTimeStamp(dto.getTimeStamp());
        entity.setRating(dto.getRating());
    }
}
