package agrechnev.repo;

import agrechnev.model.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Oleksiy Grechnyev on 12/11/2016.
 */
public interface CategoryEntityRepository extends JpaRepository<CategoryEntity, Long> {
}
