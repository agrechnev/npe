package agrechnev.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
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

    //-------------- Links -----------------
    @ManyToOne
    UserEntity user;

    // TODO: add comments here

    //-------------- Constructors -----------

    public PostEntity() {
    }

    public PostEntity(String title, String text, LocalDateTime timeStamp, int rating) {
        this.title = title;
        this.text = text;
        this.timeStamp = timeStamp;
        this.rating = rating;
    }

    //--------------- toString-----------------------

    //--------------- equals + hashCode -------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostEntity)) return false;

        PostEntity that = (PostEntity) o;

        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
