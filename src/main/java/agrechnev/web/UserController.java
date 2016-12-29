package agrechnev.web;

import agrechnev.dto.UserDto;
import agrechnev.model.UserRole;
import agrechnev.security.ExtraAuthService;
import agrechnev.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
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

    @Autowired
    private ExtraAuthService extraAuthService; // Some extra authentication operations

    private UserService userService;


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
     * Get user Dto by id
     *
     * @param userId
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{userId}")
    public UserDto get(@PathVariable Long userId) {
        UserDto result = userService.get(userId);

        // Invalidate password
        result.setPassw("CYBERDEMON");

        return result;
    }

    ;

    /**
     * Create a new user
     *
     * @param userDto
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody UserDto userDto) {
        logger.info("Creating new user :" + userDto.getLogin());

        userDto.setId(0L); // Just to be safe

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

    /**
     * Update user by Id (full name and e-mail only, NOT password)
     *
     * @param userId
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/{userId}")
    public void update(@PathVariable Long userId, @RequestBody UserDto updator) {
        logger.info("Updating user with id=" + userId);

        UserDto oldDto = userService.get(userId); // Get data for this id
        logger.info("login=" + oldDto.getLogin());

        // Update 2 fields only
        oldDto.setFullName(updator.getFullName());
        oldDto.setEmail(updator.getEmail());

        userService.update(oldDto);
        logger.info("Update successful");
    }

    /**
     * Delete my account. I made it a POST in order to send the password string
     * Requires authorization and an extra password check
     * Yes, I can create a DELETE request with a body as well, but let's keep it simple
     *
     * @param userId
     */
    @RequestMapping(method = RequestMethod.POST, value = "/{userId}/delete_account")
    public ResponseEntity deleteMyAccount(@PathVariable Long userId,
                                          @RequestBody String passw, Principal principal) {
        logger.info("Deleting user with id=" + userId);
        // Check that both userId and password are correct
        if (extraAuthService.authenticates(userId, passw) && (userId.equals(extraAuthService.getId(principal)))) {
            userService.delete(userId);
            logger.info("Delete successful");
            return ResponseEntity.ok(null);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    /**
     * Delete any user account (admin only)
     *
     * @param userId
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/{userId}")
    public void delete(@PathVariable Long userId) {
        logger.info("(Admin) Deleting user with id=" + userId);

        userService.delete(userId);
        logger.info("Delete successful");

    }
}
