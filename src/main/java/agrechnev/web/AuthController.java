package agrechnev.web;

import agrechnev.dto.UserDto;
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

        UserDto userDto = userService.findByLogin(user.getName());

        // Will be null if empty database and admin:admin
        // To avoid ugly (although harmless) NullPointerException
        return userDto == null ? 0L : userDto.getId();
    }

    // POST /logout should work by itself as POST
}
