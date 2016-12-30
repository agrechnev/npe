package agrechnev.web;

import agrechnev.dto.PostDto;
import agrechnev.security.ExtraAuthService;
import agrechnev.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;

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
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

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
}
