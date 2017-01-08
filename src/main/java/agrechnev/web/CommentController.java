package agrechnev.web;

import agrechnev.dto.CommentDto;
import agrechnev.security.ExtraAuthService;
import agrechnev.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * Created by Oleksiy Grechnyev on 1/8/2017.
 */
@RequestMapping("/rest/post/{postId}/comment")
@RestController
public class CommentController {

    private CommentService commentService;
    private ExtraAuthService extraAuthService; // Some extra authentication operations

    @Autowired
    public CommentController(CommentService commentService, ExtraAuthService extraAuthService) {
        this.commentService = commentService;
        this.extraAuthService = extraAuthService;
    }

    /**
     * Get all comments of one post
     *
     * @param postId
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Collection<CommentDto> getAll(@PathVariable Long postId) {
        return commentService.findByPostId(postId);
    }

}
