package agrechnev.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Oleksiy Grechnyev on 12/29/2016.
 * Post DTO for the NPE project
 */
public class PostDto implements Dto {

    private Long id; // Unique id

    //------------- Proper fields -----------------

    String title; // Post title

    String text;  // Post text

    LocalDateTime timeStamp;

    int rating; // Post rating, normally starts from 0

    Long userId; // Corresponds to PostEntity.user

    // Categories (represented by id)
    Set<Long> categories = new HashSet<>();

    // Extra data: userd on read operation only

    String userLogin; // The user login corresponding to userId

    // Does this post belong to the user currently logged in ?
    // Set by the Web controller
    boolean editable;

    //-------------- Constructors ------------------

    public PostDto() {
        this.editable = false;
    }

    public PostDto(String title, String text, LocalDateTime timeStamp, int rating) {
        this.title = title;
        this.text = text;
        this.timeStamp = timeStamp;
        this.rating = rating;

        this.editable = false;
    }

    //--------------- getters + setters-------------

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<Long> getCategories() {
        return categories;
    }

    public void setCategories(Set<Long> categories) {
        this.categories = categories;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public boolean getEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    //--------------- toString-----------------------

    //--------------- equals + hashCode -------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostDto)) return false;

        PostDto postDto = (PostDto) o;

        return getId() != null ? getId().equals(postDto.getId()) : postDto.getId() == null;

    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
