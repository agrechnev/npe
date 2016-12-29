package agrechnev.security;

import agrechnev.dto.UserDto;
import agrechnev.model.UserRole;
import agrechnev.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

/**
 * Created by Oleksiy Grechnyev on 12/29/2016.
 * Additional authentication operations
 */
@Service
public class ExtraAuthService {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Check if user with this id authenticates with given RAW password (NOT already encoded)
     *
     * @param id
     * @param password
     * @return
     */
    public boolean authenticates(Long id, String password) {
        UserDto userDto = userService.get(id);

        return (userDto == null) ? false : passwordEncoder.matches(password, userDto.getPassw());
    }

    /**
     * Get id of the user identified by the principal
     *
     * @param principal
     * @return
     */
    public Long getId(Principal principal) {
        UserDto userDto = userService.findByLogin(principal.getName());
        return (userDto == null) ? null : userDto.getId();
    }

    /**
     * Check if the user is an admin
     *
     * @param principal
     * @return
     */
    public boolean isAdmin(Principal principal) {
        UserDto userDto = userService.findByLogin(principal.getName());
        return (userDto == null) ? false : (userDto.getRole() == UserRole.ADMIN);
    }
}
