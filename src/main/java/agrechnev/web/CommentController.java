package agrechnev.web;

import agrechnev.dto.CommentDto;
import agrechnev.security.ExtraAuthService;
import agrechnev.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.springframework.http.ResponseEntity.status;

/**
 * Created by Oleksiy Grechnyev on 1/8/2017.
 */
@RequestMapping("/rest/post/{postId}/comment")
@RestController
public class CommentController {

    public static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    private CommentService commentService;
    private ExtraAuthService extraAuthService; // Some extra authentication operations

    @Autowired
    public CommentController(CommentService commentService, ExtraAuthService extraAuthService) {
        this.commentService = commentService;
        this.extraAuthService = extraAuthService;
    }

    /**
     * Set up the editable and deletable flags in the comment
     *
     * @param commentDto
     * @param principal
     */
    private void setEditDelete(CommentDto commentDto, Principal principal) {
        if (principal == null) {
            // Not logged in
            commentDto.setEditable(false);
            commentDto.setDeletable(false);
        } else if (extraAuthService.getId(principal).equals(commentDto.getUserId())) {
            // Comment owner can delete or edit
            commentDto.setEditable(true);
            commentDto.setDeletable(true);
        } else if (extraAuthService.isAdmin(principal) ||
                extraAuthService.getId(principal).equals(commentDto.getPostOwnerId())) {
            // Admin and post owner can delete only
            commentDto.setEditable(false);
            commentDto.setDeletable(true);
        } else {
            // Any other user
            commentDto.setEditable(false);
            commentDto.setDeletable(false);
        }
    }


    /**
     * Get all comments of one post
     *
     * @param postId
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Collection<CommentDto> getAll(@PathVariable Long postId, Principal principal) {
        List<CommentDto> comments = commentService.findByPostId(postId);

        // Set up flags
        for (CommentDto c : comments) setEditDelete(c, principal);

        return comments;
    }

    /**
     * Get one comment
     *
     * @param postId
     * @param id
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<CommentDto> get(@PathVariable Long postId,
                                          @PathVariable Long id, Principal principal) {
        CommentDto commentDto = commentService.get(id);

        if (commentDto == null || !commentDto.getPostId().equals(postId)) {
            return status(HttpStatus.NOT_FOUND).body(null);
        }

        // Set up flags
        setEditDelete(commentDto, principal);

        return ResponseEntity.ok(commentDto);
    }


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@PathVariable Long postId,
                                    @RequestBody CommentDto commentDto, Principal principal) {


        // Set comment owner
        Long userId = extraAuthService.getId(principal);
        if (userId == null) return status(HttpStatus.UNAUTHORIZED).body(null);

        commentDto.setUserId(userId);

        String shortText = commentDto.getText().substring(0, Math.min(commentDto.getText().length(), 100));

        logger.info("Creating new comment:" + shortText);
        logger.info("by user : " + commentDto.getUserId());

        // Set rating
        commentDto.setRating(0);

        // Set timestamp
        commentDto.setTimeStamp(LocalDateTime.now());

        // Set post id
        commentDto.setPostId(postId);

        // Create a new comment
        Long id = commentService.create(commentDto);

        // Create new post URI
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();

        logger.info("Creation successful");

        // Return the new location
        return ResponseEntity.created(uri).build();
    }

    /**
     * Delete a comment id
     *
     * @param postId
     * @param id
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long postId,
                                    @PathVariable Long id, Principal principal) {

        logger.info("Deleting a comment : " + id);
        // Check if the comment exists and postId is right
        CommentDto commentDto = commentService.get(id);
        if (commentDto == null || !commentDto.getPostId().equals(postId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if (extraAuthService.isAdmin(principal) ||
                extraAuthService.getId(principal).equals(commentDto.getUserId()) ||
                extraAuthService.getId(principal).equals(commentDto.getPostOwnerId())) {

            // Admin, comment owner, or post owner == OK, may delete

            commentService.delete(id);

            logger.info("Delete successful.");
            return ResponseEntity.ok(null);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    /**
     * Update a comment
     *
     * @param postId
     * @param id
     * @param updator
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<?> update(@PathVariable Long postId,
                                    @PathVariable Long id,
                                    @RequestBody CommentDto updator,
                                    Principal principal) {

        logger.info("Updating a comment : " + id);
        // Check if the comment exists and postId is right
        CommentDto commentDto = commentService.get(id);
        if (commentDto == null || !commentDto.getPostId().equals(postId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Only comment owner may edit
        if (extraAuthService.getId(principal).equals(commentDto.getUserId())) {

            // Admin, comment owner, or post owner == OK, may delete

            // Change text only
            commentDto.setText(updator.getText());

            commentService.update(commentDto);

            logger.info("Update successful.");
            return ResponseEntity.ok(null);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }
}
