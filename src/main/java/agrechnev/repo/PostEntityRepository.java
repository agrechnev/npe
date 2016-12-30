package agrechnev.repo;

import agrechnev.model.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Oleksiy Grechnyev on 12/11/2016.
 * Repo for PostEntity
 */
public interface PostEntityRepository extends JpaRepository<PostEntity, Long> {
}
