package agrechnev.dto;

/**
 * Created by Oleksiy Grechnyev on 12/18/2016.
 * Interface with getId() and setId() for my dtos
 * DTO's ID always coincides with the respective database entity's ID
 */
public interface Dto {
    public Long getId();

    public void setId(Long id);
}
