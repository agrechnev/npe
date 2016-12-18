package agrechnev.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Oleksiy Grechnyev on 11/21/2016.
 * The category entity for the NPE project
 * E.g. "java", "c#" or "python"
 */
@Entity
public class CategoryEntity implements EntityWithId {
    @Id
    @GeneratedValue
    private Long id;  // Unique category id

    @Column(nullable = false)
    private String categoryName; // E.g. "java", "java-spring" or "java-spring-hateoas"

    //---------------------------------------------------------
    // Constructors
    public CategoryEntity() {
    }

    public CategoryEntity(String categoryName) {
        this.categoryName = categoryName;
    }

    //---------------------------------------------------------
    // Getters+Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    //----------------------------------------------------------
    // toString()

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CategoryEntity{");
        sb.append("id=").append(id);
        sb.append(", categoryName='").append(categoryName).append('\'');
        sb.append('}');
        return sb.toString();
    }

    //----------------------------------------------------------
    // equals() + hashCode()


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryEntity)) return false;

        CategoryEntity that = (CategoryEntity) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;

    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
