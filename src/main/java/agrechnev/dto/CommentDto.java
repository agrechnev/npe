package agrechnev.dto;

import java.time.LocalDateTime;

/**
 * Created by Oleksiy Grechnyev on 1/8/2017.
 * Dto for Comment
 */
public class CommentDto implements Dto {
    private Long id; // Unique id

    //------------- Proper fields -----------------

    private String text; // Comment text

    private LocalDateTime timeStamp;

    private int rating; // Comment rating, normally starts from 0

    //------------- Links -------------------
    private Long userId; // Corresponds to CommentEntity.user

    private Long postId; // Corresponds to CommentEntity.post

    //-------------- Constructors ------------------
    public CommentDto() {
    }

    public CommentDto(String text, LocalDateTime timeStamp, int rating) {
        this.text = text;
        this.timeStamp = timeStamp;
        this.rating = rating;
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

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    //--------------- toString-----------------------

    //--------------- equals + hashCode -------------


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentDto)) return false;

        CommentDto that = (CommentDto) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;

    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
