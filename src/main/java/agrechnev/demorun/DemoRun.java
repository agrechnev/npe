package agrechnev.demorun;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Oleksiy Grechnyev on 12/7/2016.
 * A bean to do some fun stuff from the main program,
 * e.g. to print the database
 */
@Repository
public class DemoRun {
//    @Autowired
//    EntityManagerFactory entityManagerFactory;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    PersonRepository personRepository;

/*    public void createData(){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Person brianna=new Person("Brianna");
        entityManager.persist(brianna);
        Person mira=new Person("Mira");
        entityManager.persist(mira);
        Book book1=new Book("Handmaiden's War");
//                book1.getAuthors().add(brianna);
        entityManager.persist(book1);
        Book book2=new Book("Handmaiden's Love");
//                book2.getAuthors().add(brianna);
        entityManager.persist(book2);
        brianna.getBooks().add(book1);
        brianna.getBooks().add(book2);
        mira.getBooks().add(book1);
        mira.getBooks().add(book2);
        entityManager.getTransaction().commit();
    }*/

    @Transactional
    // Create data using JPA repos
    public void createDataRepo() {
        // Clean up first
        bookRepository.deleteAllInBatch();
        personRepository.deleteAllInBatch();

        // Now let's create some data
        Person brianna = new Person("Brianna");
        personRepository.save(brianna);

        Person mira = new Person("Mira");
        personRepository.save(mira);

        Book book1 = new Book("Handmaiden's War");
//                book1.getAuthors().add(brianna);
        bookRepository.save(book1);

        Book book2 = new Book("Handmaiden's Love");
//                book2.getAuthors().add(brianna);
        bookRepository.save(book2);

        brianna.getBooks().add(book1);
        brianna.getBooks().add(book2);

        mira.getBooks().add(book1);
        mira.getBooks().add(book2);
    }

/*    public void printData() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        // Query all persons
        List<Person> personList = entityManager
                .createQuery("from Person", Person.class).getResultList();
        System.out.println("");
        System.out.println("Persons :");
        System.out.println("");
        personList.forEach(System.out::println);
        // Query all books
        List<Book> bookList = entityManager
                .createQuery("from Book", Book.class).getResultList();
        System.out.println("");
        System.out.println("Books :");
        System.out.println("");
        bookList.forEach(System.out::println);
        entityManager.getTransaction().commit();
        entityManager.close();
    }*/

    @Transactional
    public void printDataRepo() {
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println("Persons from PersonRepository:");

        List<Person> personList = personRepository.findAll();
        personList.forEach(System.out::println);

        System.out.println("Books from BookRepository:");

        List<Book> bookList = bookRepository.findAll();
        bookList.forEach(System.out::println);
    }
}
