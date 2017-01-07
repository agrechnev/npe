package agrechnev.web;

import agrechnev.dto.CategoryDto;
import agrechnev.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * Created by Oleksiy Grechnyev on 1/7/2017.
 */
@RestController
@RequestMapping("/rest/category")
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Collection<CategoryDto> getAll() {
        return categoryService.getAll();
    }
}
