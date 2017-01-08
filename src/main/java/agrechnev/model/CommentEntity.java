package agrechnev.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by Oleksiy Grechnyev on 1/8/2017.
 * Comment (to the post) entity for the NPE project
 */
@Entity
public class CommentEntity implements EntityWithId {
    @Id
    @GeneratedValue
    private Long id; // Unique id
    //------------- Proper fields -----------------

    @Column(nullable = false)
    @Type(type = "text")
    private String text; // Comment text

    @Column(nullable = false)
    private LocalDateTime timeStamp;

    private int rating; // Comment rating, normally starts from 0

    //-------------- Links -----------------

    // Comment author
    @ManyToOne
    private UserEntity user;

    // Post this commnet belongs to
    @ManyToOne
    private PostEntity post;

    //-------------- Constructors ------------------

    public CommentEntity() {
    }

    public CommentEntity(String text, LocalDateTime timeStamp, int rating) {
        this.text = text;
        this.timeStamp = timeStamp;
        this.rating = rating;
    }

    //--------------- getters + setters----------------

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public PostEntity getPost() {
        return post;
    }

    public void setPost(PostEntity post) {
        this.post = post;
    }

    //--------------- toString-----------------------

    //--------------- equals + hashCode -------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentEntity)) return false;

        CommentEntity that = (CommentEntity) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;

    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
