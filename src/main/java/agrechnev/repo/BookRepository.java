package agrechnev.repo;

import agrechnev.demorun.Book;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Oleksiy Grechnyev on 12/9/2016.
 */
public interface BookRepository extends JpaRepository<Book, Long> {
}
