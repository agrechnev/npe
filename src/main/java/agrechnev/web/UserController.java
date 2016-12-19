package agrechnev.web;

import agrechnev.dto.UserDto;
import agrechnev.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;

/**
 * User account controller
 * Created by Oleksiy Grechnyev on 12/11/2016.
 */
@RequestMapping("/rest/user")
@RestController
public class UserController {
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Get all users
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Collection<UserDto> getAll() {
        return userService.getAll();
    }

    /**
     * Create a new user
     *
     * @param userDto
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody UserDto userDto) {
        System.out.println("Creating new user :" + userDto);

        Long id = userService.create(userDto);

        // Create new user's URI
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();

        // Return the new location
        return ResponseEntity.created(uri).build();
    }
}
