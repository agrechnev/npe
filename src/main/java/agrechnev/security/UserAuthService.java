package agrechnev.security;

import agrechnev.model.UserEntity;
import agrechnev.model.UserRole;
import agrechnev.repo.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Oleksiy Grechnyev on 12/19/2016.
 * Authorize users using User Entity Repo
 */
@Service
public class UserAuthService implements UserDetailsService {
    @Autowired
    UserEntityRepository userEntityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // List of authorities includes USER at least
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        // Check if the repo is empty
        if (userEntityRepository.count() == 0) {
            // Authorize admin:admin for empty repo
            if (username.equals("admin")) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                return new User("admin", "admin", authorities);
            } else {
                throw new UsernameNotFoundException(username);
            }
        }

        // Work with the non-empty repo
        Optional<UserEntity> o = userEntityRepository.findByLogin(username);
        if (o.isPresent()) {
            // User found
            // Add admin role if needed
            if (o.get().getRole() == UserRole.ADMIN) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }
            // Authorize
            return new User(username, o.get().getPassw(), authorities);

        } else {
            // User not found
            throw new UsernameNotFoundException(username);
        }
    }
}
