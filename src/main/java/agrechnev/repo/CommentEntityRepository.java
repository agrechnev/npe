package agrechnev.repo;

import agrechnev.model.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Oleksiy Grechnyev on 12/11/2016.
 * Repo for CommentEntity
 */
public interface CommentEntityRepository extends JpaRepository<CommentEntity, Long> {
    public List<CommentEntity> findByPostId(Long id);
}
