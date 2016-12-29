package agrechnev.web;

import agrechnev.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Log In and Log Out controller
 * Created by Oleksiy Grechnyev on 12/11/2016.
 */
@RestController
public class AuthController {

    @Autowired
    UserService userService;

    // Get current user as principal
    @RequestMapping(method = RequestMethod.GET, value = "/userauth")
    public Principal user(Principal user) {
        return user;
    }

    // Get current user ID if authenticated
    @RequestMapping(method = RequestMethod.GET, value = "/userid")
    public Long userId(Principal user) {
        // NullPointerException could happen here, but it would be pathological anyway
        return userService.findByLogin(user.getName()).getId();
    }

    // POST /logout should work by itself as POST
}
