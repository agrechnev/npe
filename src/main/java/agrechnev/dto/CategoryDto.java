package agrechnev.dto;

/**
 * Created by Oleksiy Grechnyev on 1/7/2017.
 * Category DTO
 */
public class CategoryDto implements Dto {
    private Long id; // Unique id

    //------------- Proper fields -----------------

    private String categoryName; // E.g. "java", "java-spring" or "java-spring-hateoas"

    //-------------- Constructors ------------------


    public CategoryDto() {
    }

    public CategoryDto(String categoryName) {
        this.categoryName = categoryName;
    }

    //--------------- getters + setters-------------


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
