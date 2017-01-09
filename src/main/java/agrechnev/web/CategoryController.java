package agrechnev.web;

import agrechnev.dto.CategoryDto;
import agrechnev.security.ExtraAuthService;
import agrechnev.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.Collection;
import java.util.List;

import static org.springframework.http.ResponseEntity.status;

/**
 * Created by Oleksiy Grechnyev on 1/7/2017.
 */
@RestController
@RequestMapping("/rest/category")
public class CategoryController {

    public static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    //Services
    private CategoryService categoryService;

    private ExtraAuthService extraAuthService;

    @Autowired
    public CategoryController(CategoryService categoryService, ExtraAuthService extraAuthService) {
        this.categoryService = categoryService;
        this.extraAuthService = extraAuthService;
    }


    /**
     * Get all categories, sort alphabetically
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Collection<CategoryDto> getAll() {
        List<CategoryDto> categories = categoryService.getAll();

        // Sort by name
        categories.sort((c1, c2) -> c1.getCategoryName().compareTo(c2.getCategoryName()));

        return categories;
    }

    /**
     * Get one category
     *
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<CategoryDto> get(@PathVariable Long id) {
        CategoryDto categoryDto = categoryService.get(id);

        if (categoryDto == null) {
            return status(HttpStatus.NOT_FOUND).body(null);
        } else {
            return ResponseEntity.ok(categoryDto);
        }
    }

    /**
     * Create a new category
     *
     * @param categoryDto
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody CategoryDto categoryDto, Principal principal) {

        logger.info("Creating new category " + categoryDto.getCategoryName());

        Long userId = extraAuthService.getId(principal);
        logger.info("by user : " + userId);

        // Check if we are legit
        if (!extraAuthService.isExpertOrAdmin(principal)) {
            return status(HttpStatus.FORBIDDEN).body(null);
        }

        Long id = categoryService.create(categoryDto); // No troubles here

        // Create new post URI
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();

        logger.info("Creation successful");

        // Return the new location
        return ResponseEntity.created(uri).build();
    }

    /**
     * Delete a category
     *
     * @param id
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Principal principal) {
        logger.info("Deleting category " + id);

        if (extraAuthService.isAdmin(principal)) {
            categoryService.delete(id);
            logger.info("Delete successful.");

            return ResponseEntity.ok(null);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    /**
     * Update a category
     *
     * @param id
     * @param updator
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody CategoryDto updator,
                                    Principal principal) {

        logger.info("Updating a category : " + id);

        // Check if the category exists and postId is right
        CategoryDto categoryDto = categoryService.get(id);
        if (categoryDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Only admin
        if (extraAuthService.isAdmin(principal)) {

            // Change name only
            categoryDto.setCategoryName(updator.getCategoryName());

            categoryService.update(categoryDto);

            logger.info("Update successful.");
            return ResponseEntity.ok(null);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

}
