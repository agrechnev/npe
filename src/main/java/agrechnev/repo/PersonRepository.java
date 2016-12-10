package agrechnev.repo;

import agrechnev.demorun.Person;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Oleksiy Grechnyev on 12/9/2016.
 */
public interface PersonRepository extends JpaRepository<Person, Long> {
}
