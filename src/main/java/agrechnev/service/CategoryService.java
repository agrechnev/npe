package agrechnev.service;

import agrechnev.dto.CategoryDto;
import agrechnev.helpers.Util;
import agrechnev.model.CategoryEntity;
import agrechnev.repo.CategoryEntityRepository;
import org.springframework.stereotype.Service;

/**
 * Service for the Category DTOs
 * Created by Oleksiy Grechnyev on 1/7/2017.
 */
@Service
public class CategoryService extends AbstractService<CategoryDto, CategoryEntity> {

    // Constructor
    public CategoryService(CategoryEntityRepository categoryEntityRepository) {
        this.entityRepo = categoryEntityRepository;
    }

    /**
     * Check if the Dto is valid (disregarding ID)
     * E.g. check if there are no null or empty fields
     *
     * @param dto
     * @return
     */
    @Override
    public boolean isValid(CategoryDto dto) {
        return !Util.isEmptyString(dto.getCategoryName());
    }

    /**
     * Convert dto to entity (I chose to do things by hand, not with dozer or something)
     *
     * @param dto
     * @return
     */
    @Override
    protected CategoryEntity Dto2Entity(CategoryDto dto) {
        CategoryEntity entity = new CategoryEntity(dto.getCategoryName());
        entity.setId(dto.getId());
        return entity;
    }

    /**
     * Convert Entity to Dto
     *
     * @param entity
     * @return
     */
    @Override
    protected CategoryDto Entity2Dto(CategoryEntity entity) {
        CategoryDto dto = new CategoryDto(entity.getCategoryName());
        dto.setId(entity.getId());
        return dto;
    }

    /**
     * Update entity with a Dto leaving all other fields (id, links) untouched
     *
     * @param entity
     * @param dto
     */
    @Override
    protected void updateEntity(CategoryEntity entity, CategoryDto dto) {
        entity.setCategoryName(dto.getCategoryName());
    }
}
