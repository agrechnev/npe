package agrechnev.model;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Oleksiy Grechnyev on 12/10/2016.
 * Post entity for the NPE project
 */
@Entity
public class PostEntity implements EntityWithId {

    @Id
    @GeneratedValue
    private Long id; // Unique id

    //------------- Proper fields -----------------
    @Column(nullable = false)
    private String title; // Post title

    @Column(nullable = false)
    @Type(type = "text")
    private String text;  // Post text

    @Column(nullable = false)
    private LocalDateTime timeStamp;

    private int rating; // Post rating, normally starts from 0

    //-------------- Links -----------------
    // Categories of this post
    @ManyToMany
    @Cascade({CascadeType.PERSIST, CascadeType.MERGE})
    private Set<CategoryEntity> categories = new HashSet<>();

    // Post owner+author
    @ManyToOne
    private UserEntity user;

    // Comments
    @OneToMany(orphanRemoval = true, mappedBy = "post")
    @Cascade(CascadeType.ALL)
    private List<CommentEntity> comments = new ArrayList<>();

    //-------------- Constructors -----------

    public PostEntity() {
    }

    public PostEntity(String title, String text, LocalDateTime timeStamp, int rating) {
        this.title = title;
        this.text = text;
        this.timeStamp = timeStamp;
        this.rating = rating;
    }
    //--------------- getters + setters-----------------------

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Set<CategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(Set<CategoryEntity> categories) {
        this.categories = categories;
    }

    public List<CommentEntity> getComments() {
        return comments;
    }

    public void setComments(List<CommentEntity> comments) {
        this.comments = comments;
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
