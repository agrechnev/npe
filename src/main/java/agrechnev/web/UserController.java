package agrechnev.web;

import agrechnev.dto.UserDto;
import agrechnev.model.UserRole;
import agrechnev.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.List;

/**
 * User account controller
 * Created by Oleksiy Grechnyev on 12/11/2016.
 */
@RequestMapping("/rest/user")
@RestController
public class UserController {
    public static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private PasswordEncoder passwordEncoder; // The password encoder

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

        // Do not give passwords to the client
        List<UserDto> all = userService.getAll();
        for (UserDto userDto : all) {
            userDto.setPassw("CYBERDEMON");
        }
        return all;
    }

    /**
     * Create a new user
     *
     * @param userDto
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody UserDto userDto) {
        logger.info("Creating new user :" + userDto.getLogin());

        // Set role and points: no cheating !!!
        userDto.setRole(UserRole.USER);
        userDto.setPoints(0);

        // Encode the password
        userDto.setPassw(passwordEncoder.encode(userDto.getPassw()));

        Long id = userService.create(userDto);

        // Create new user's URI
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();

        // Return the new location
        return ResponseEntity.created(uri).build();
    }
}
