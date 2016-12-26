package agrechnev.sampledb;

import agrechnev.model.CategoryEntity;
import agrechnev.model.UserEntity;
import agrechnev.model.UserRole;
import agrechnev.repo.CategoryEntityRepository;
import agrechnev.repo.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Oleksiy Grechnyev on 12/17/2016.
 * Create sample database for NPE
 * This class must be used as a bean
 */
@Component
public class SampleDB {
    /**
     * Create sample database for NPE
     * Erases all previous data
     */
    @Autowired
    UserEntityRepository userEntityRepository;

    @Autowired
    CategoryEntityRepository categoryEntityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // The password encoder

    @Transactional
    public void createSampleDB() {
        // Delete everything first
        userEntityRepository.deleteAllInBatch();
        categoryEntityRepository.deleteAllInBatch();

        // Let's create categories
        CategoryEntity catJava = new CategoryEntity("java");
        categoryEntityRepository.save(catJava);
        CategoryEntity catJavaSpring = new CategoryEntity("java-spring");
        categoryEntityRepository.save(catJavaSpring);
        CategoryEntity catJavaSpringBoot = new CategoryEntity("java-spring-boot");
        categoryEntityRepository.save(catJavaSpringBoot);
        CategoryEntity catJavaSpringAOP = new CategoryEntity("java-spring-aop");
        categoryEntityRepository.save(catJavaSpringAOP);
        CategoryEntity catJavaHibernate = new CategoryEntity("java-hibernate");
        categoryEntityRepository.save(catJavaHibernate);
        CategoryEntity catPython = new CategoryEntity("python");
        categoryEntityRepository.save(catPython);
        CategoryEntity catCSharp = new CategoryEntity("c#");
        categoryEntityRepository.save(catCSharp);
        CategoryEntity catCPP = new CategoryEntity("c++");
        categoryEntityRepository.save(catCPP);

        // Let's create some users, uses passwordEncoder
        UserEntity userBrianna = new UserEntity("brianna", passwordEncoder.encode("brianna"),
                "Brianna The Handmaiden", "brianna@gmail.com", 100500, UserRole.EXPERT);
        userEntityRepository.save(userBrianna);

        userBrianna.getMyCategories().add(catJava);
        userBrianna.getMyCategories().add(catCPP);
        userBrianna.getMyCategories().add(catCSharp);

        UserEntity userAdmin = new UserEntity("admin", passwordEncoder.encode("admin"),
                "Site administrator", "webmaster@npe.com", 10, UserRole.ADMIN);
        userEntityRepository.save(userAdmin);
    }
}
