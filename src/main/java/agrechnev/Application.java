package agrechnev;

import agrechnev.demorun.DemoRun2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * Created by Oleksiy Grechnyev on 12/5/2016.
 */

@SpringBootApplication
public class Application {

    public static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Application.class, args);
    }


    @Bean
    public CommandLineRunner demo(DemoRun2 demoRun2) {
        return args -> {
            logger.info("I am Brianna and I am a jedi !!!");

            // Fun with the DB

//            demoRun2.create();
            demoRun2.print();
        };
    }

}
