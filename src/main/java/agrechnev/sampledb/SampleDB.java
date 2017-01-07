package agrechnev.sampledb;

import agrechnev.model.CategoryEntity;
import agrechnev.model.PostEntity;
import agrechnev.model.UserEntity;
import agrechnev.model.UserRole;
import agrechnev.repo.CategoryEntityRepository;
import agrechnev.repo.PostEntityRepository;
import agrechnev.repo.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
    PostEntityRepository postEntityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // The password encoder

    /**
     * Create sample database
     */

    @Transactional
    public void createSampleDB() {
        deleteEverything();

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
        CategoryEntity catJavascript = new CategoryEntity("javascript");
        categoryEntityRepository.save(catJavascript);
        CategoryEntity catJavascriptAngular = new CategoryEntity("javascript-angular");
        categoryEntityRepository.save(catJavascriptAngular);

        // Let's create some users, uses passwordEncoder
        UserEntity userBrianna = new UserEntity("brianna", passwordEncoder.encode("brianna"),
                "Brianna The Handmaiden", "brianna@gmail.com", 100500, UserRole.EXPERT);
        userEntityRepository.save(userBrianna);

        userBrianna.getMyCategories().add(catJava);
        userBrianna.getMyCategories().add(catCPP);
        userBrianna.getMyCategories().add(catCSharp);

        UserEntity userSeymour = new UserEntity("seymour", passwordEncoder.encode("seymour"),
                "Seymour Guado", "seymour@yahoo.com", 108, UserRole.USER);
        userEntityRepository.save(userSeymour);

        userSeymour.getMyCategories().add(catJava);
        userSeymour.getMyCategories().add(catJavaHibernate);
        userSeymour.getMyCategories().add(catJavaSpring);
        userSeymour.getMyCategories().add(catJavaSpringAOP);
        userSeymour.getMyCategories().add(catJavaSpringBoot);


        // Let's create some posts
        PostEntity post1 = new PostEntity("$http.delete() with a body in Angular JS",
                "Why cannot I make a $http.delete() request with a body, like " +
                        "in the post() request?", LocalDateTime.now(), 0);
        post1.setUser(userBrianna);
        post1.getCategories().add(catJavascript);
        post1.getCategories().add(catJavascriptAngular);

        postEntityRepository.save(post1);
    }

    /**
     * Delete everything and create admin:admin
     */
    public void deleteEverything() {
        // Delete everything first
        postEntityRepository.deleteAllInBatch();
        userEntityRepository.deleteAllInBatch();
        categoryEntityRepository.deleteAllInBatch();

        // Create the admin account
        UserEntity userAdmin = new UserEntity("admin", passwordEncoder.encode("admin"),
                "Site administrator", "webmaster@npe.com", 10, UserRole.ADMIN);
        userEntityRepository.save(userAdmin);
    }
}
