package agrechnev.security;

import agrechnev.dto.UserDto;
import agrechnev.model.UserRole;
import agrechnev.repo.UserEntityRepository;
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

    // This is needed to handle admin:admin in an empty DB
    @Autowired
    private UserEntityRepository userEntityRepository;

    /**
     * Check if user with this id authenticates with given RAW password (NOT already encoded)
     *
     * @param id
     * @param password
     * @return
     */
    public boolean authenticates(Long id, String password) {

        // The empty DB case
        if (userEntityRepository.count() == 0) {
            return password.equals("admin");
        }

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
        if (principal == null) {
            return null;
        }

        // The empty DB case
        if (userEntityRepository.count() == 0) {
            return 0L;
        }

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

        // The empty DB case
        if (userEntityRepository.count() == 0) {
            return principal.getName().equals("admin");
        }

        if (principal == null) {
            return false;
        }

        UserDto userDto = userService.findByLogin(principal.getName());
        return (userDto == null) ? false : (userDto.getRole() == UserRole.ADMIN);
    }

    /**
     * Check if the user is expert or admin
     *
     * @param principal
     * @return
     */
    public boolean isExpertOrAdmin(Principal principal) {

        // The empty DB case
        if (userEntityRepository.count() == 0) {
            return principal.getName().equals("admin");
        }

        if (principal == null) {
            return false;
        }

        UserDto userDto = userService.findByLogin(principal.getName());
        return (userDto == null) ? false :
                (userDto.getRole() == UserRole.ADMIN || userDto.getRole() == UserRole.EXPERT);
    }
}
