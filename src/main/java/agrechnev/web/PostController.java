package agrechnev.web;

import agrechnev.dto.PostDto;
import agrechnev.security.ExtraAuthService;
import agrechnev.service.PostService;
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

import static org.springframework.http.ResponseEntity.status;

/**
 * Posts controller
 * Created by Oleksiy Grechnyev on 12/29/2016.
 * Posts can be: viewed by everyone
 * Created by any registered user
 * Updated/deleted by owner
 */
@RequestMapping("/rest/post")
@RestController
public class PostController {
    public static final Logger logger = LoggerFactory.getLogger(PostController.class);


    private PostService postService;

    private ExtraAuthService extraAuthService; // Some extra authentication operations

    // Constructor
    @Autowired
    public PostController(PostService postService, ExtraAuthService extraAuthService) {
        this.postService = postService;
        this.extraAuthService = extraAuthService;
    }

    /**
     * Get all posts
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Collection<PostDto> getAll() {
        return postService.getAll();
    }

    /**
     * Create a new post
     *
     * @param postDto
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody PostDto postDto, Principal principal) {
        logger.info("Creating new post :" + postDto.getTitle());
        logger.info("by user : " + postDto.getUserId());

        // Set the timestamp
        postDto.setTimeStamp(LocalDateTime.now());

        // Set the user id
        Long userId = extraAuthService.getId(principal);
        if (userId == null) return status(HttpStatus.UNAUTHORIZED).body(null);

        postDto.setUserId(userId);

        Long id = postService.create(postDto);

        // Create new post URI
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();

        logger.info("Creation successful");

        // Return the new location
        return ResponseEntity.created(uri).build();
    }

    /**
     * Get one post
     *
     * @param postId
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{postId}")
    public ResponseEntity<PostDto> get(@PathVariable Long postId, Principal principal) {

        PostDto postDto = postService.get(postId);

        if (postDto == null) return status(HttpStatus.NOT_FOUND).body(null);

        // Check if the post belongs to the current user if logged in
        if (principal != null && postDto.getUserId().equals(extraAuthService.getId(principal))) {
            postDto.setEditable(true);
        }

        return ResponseEntity.ok(postDto);
    }

    /**
     * Delete a post (post owner or admin)
     *
     * @param postId
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/{postId}")
    public ResponseEntity<?> delete(@PathVariable Long postId, Principal principal) {
        // Allowed for post owner or admin
        logger.info("Deleting post :" + postId);

        // Get this post
        PostDto postDto = postService.get(postId);
        if (postDto == null) return status(HttpStatus.NOT_FOUND).body(null);

        // Check if admin or owner
        if (extraAuthService.isAdmin(principal) || postDto.getUserId().equals(extraAuthService.getId(principal))) {
            postService.delete(postId);
            return ResponseEntity.ok(null);
        } else {
            return status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    /**
     * Update a post (post owner only)
     *
     * @param postId
     * @param updator
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/{postId}")
    public ResponseEntity<?> update(@PathVariable Long postId, @RequestBody PostDto updator, Principal principal) {
        // Allowed for post owner only
        logger.info("Updating post :" + postId);

        // Get this post (old version)
        PostDto oldDto = postService.get(postId);
        if (oldDto == null) return status(HttpStatus.NOT_FOUND).body(null);

        // Check if admin or owner
        if (oldDto.getUserId().equals(extraAuthService.getId(principal))) {

            // Update (text anc categories)
            oldDto.setText(updator.getText());
            oldDto.setCategories(updator.getCategories());

            postService.update(oldDto);
            return ResponseEntity.ok(null);
        } else {
            return status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
