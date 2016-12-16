package agrechnev.repo;

import agrechnev.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Oleksiy Grechnyev on 12/11/2016.
 */
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
}
