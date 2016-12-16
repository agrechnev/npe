package agrechnev.demorun;

import agrechnev.model.CategoryEntity;
import agrechnev.model.UserEntity;
import agrechnev.model.UserRole;
import agrechnev.repo.CategoryEntityRepository;
import agrechnev.repo.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Oleksiy Grechnyev on 12/11/2016.
 */
@Repository
public class DemoRun2 {

    @Autowired
    UserEntityRepository userEntityRepository;

    @Autowired
    CategoryEntityRepository categoryEntityRepository;

    @Transactional
    public void create() {
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

        // Let's create some users
        UserEntity userBrianna = new UserEntity("brianna", "brianna", "Brianna The Handmaiden",
                "brianna@gmail.com", 100500, UserRole.EXPERT);
        userEntityRepository.save(userBrianna);

        userBrianna.getMyCategories().add(catJava);
        userBrianna.getMyCategories().add(catCPP);
        userBrianna.getMyCategories().add(catCSharp);

        UserEntity userAdmin = new UserEntity("admin", "amdin", "Site administrator",
                "webmaster@npe.com", 10, UserRole.ADMIN);
        userEntityRepository.save(userAdmin);
    }

    @Transactional
    public void print() {

        System.out.println("Categories:");
        List<CategoryEntity> catList = categoryEntityRepository.findAll();
        catList.forEach(System.out::println);

        System.out.println("Users:");
        List<UserEntity> userList = userEntityRepository.findAll();
        userList.forEach(System.out::println);


    }
}
