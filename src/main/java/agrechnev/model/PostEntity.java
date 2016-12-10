package agrechnev.model;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * Created by Oleksiy Grechnyev on 12/10/2016.
 * Post entity for the NPE project
 */
@Entity
public class PostEntity {


    @Id
    @GeneratedValue
    private Long id; // Unique id

    //------------- Proper fields -----------------
    @Column(nullable = false)
    String title; // Post title

    @Column(nullable = false)
    @Type(type = "text")
    String text;  // Post text

    @Column(nullable = false)
    LocalDateTime timeStamp;

    int rating; // Post rating, normally starts from 0


}
