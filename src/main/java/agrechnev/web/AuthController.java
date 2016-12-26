package agrechnev.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Log In and Log Out controller
 * Created by Oleksiy Grechnyev on 12/11/2016.
 */
@RestController
public class AuthController {

    // Get current user
    @RequestMapping("/userauth")
    public Principal user(Principal user) {
        return user;
    }

    // /logout should work by itself, how ?
}
