package agrechnev.service;

import agrechnev.dto.CommentDto;
import agrechnev.helpers.Util;
import agrechnev.model.CommentEntity;
import agrechnev.repo.CommentEntityRepository;
import agrechnev.repo.PostEntityRepository;
import agrechnev.repo.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Oleksiy Grechnyev on 1/8/2017.
 */
@Service
public class CommentService extends AbstractService<CommentDto, CommentEntity> {

    private UserEntityRepository userEntityRepository;

    private PostEntityRepository postEntityRepository;

    // Constructor
    @Autowired
    public CommentService(CommentEntityRepository commentEntityRepository,
                          UserEntityRepository userEntityRepository,
                          PostEntityRepository postEntityRepository) {

        this.entityRepo = commentEntityRepository;
        this.userEntityRepository = userEntityRepository;
        this.postEntityRepository = postEntityRepository;
    }

    /**
     * Check if the Dto is valid (disregarding ID)
     * E.g. check if there are no null or empty fields
     *
     * @param dto
     * @return
     */
    @Override
    public boolean isValid(CommentDto dto) {
        return !Util.isEmptyString(dto.getText()) &&
                dto.getTimeStamp() != null &&
                // Check for valid user and post IDs
                userEntityRepository.exists(dto.getUserId()) &&
                postEntityRepository.exists(dto.getPostId());
    }

    /**
     * Convert dto to entity (I chose to do things by hand, not with dozer or something)
     *
     * @param dto
     * @return
     */
    @Override
    @Transactional
    protected CommentEntity Dto2Entity(CommentDto dto) {
        CommentEntity entity = new CommentEntity(dto.getText(), dto.getTimeStamp(), dto.getRating());

        // Set id
        entity.setId(dto.getId());

        // Set user
        entity.setUser(userEntityRepository.getOne(dto.getUserId()));

        // Set post
        entity.setPost(postEntityRepository.getOne(dto.getPostId()));

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
    protected CommentDto Entity2Dto(CommentEntity entity) {
        CommentDto dto = new CommentDto(entity.getText(), entity.getTimeStamp(), entity.getRating());

        // Set id
        dto.setId(entity.getId());

        // Set user
        dto.setUserId(entity.getUser().getId());

        // Set post
        dto.setPostId(entity.getPost().getId());

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
    protected void updateEntity(CommentEntity entity, CommentDto dto) {
        entity.setText(dto.getText());
        entity.setTimeStamp(dto.getTimeStamp());
        entity.setRating(dto.getRating());

        // user and post IDs are not allowed to change
    }

    //--------------------- Extra methods --------------------

    /**
     * Find comments by post ID
     *
     * @param postId
     * @return
     */
    public List<CommentDto> findByPostId(Long postId) {
        List<CommentEntity> entities = ((CommentEntityRepository) entityRepo).findByPostId(postId);

        return EntityList2DtoList(entities);
    }
}
