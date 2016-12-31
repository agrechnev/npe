package agrechnev.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * Created by Oleksiy Grechnyev on 12/17/2016.
 */
@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService; //Authentication service

    @Autowired
    private PasswordEncoder passwordEncoder; // The password encoder

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()

                .and()

                .authorizeRequests()

                // Static AngularJS pages
                .antMatchers("/*.html", "/", "/ajs/*.js").permitAll()

                // Login and logout API (AuthController)
                .antMatchers(HttpMethod.GET, "/userauth").authenticated() // Get current user principal
                .antMatchers(HttpMethod.GET, "/userid").authenticated()   // Get current user ID
                .antMatchers(HttpMethod.POST, "/logout").authenticated() // Logout

                // REST API
                // Users
                .antMatchers(HttpMethod.POST, "/rest/user").permitAll() // Create new user
                .antMatchers(HttpMethod.GET, "/rest/user").permitAll()  // TODO: Get all users
                .antMatchers(HttpMethod.DELETE, "/rest/user/*").hasRole("ADMIN")  // Admin delete
                .antMatchers(HttpMethod.POST, "/rest/user/*/delete_account").authenticated() // Delete my account
                .antMatchers(HttpMethod.POST, "/rest/user/*/change_password").authenticated() // Change password
                .antMatchers(HttpMethod.PUT, "/rest/user/*").authenticated() // Update my account
                .antMatchers(HttpMethod.GET, "/rest/user/*").authenticated() // Get my account data

                // Posts
                .antMatchers(HttpMethod.POST, "/rest/post").authenticated()  //  Create new post
                .antMatchers(HttpMethod.GET, "/rest/post").permitAll()  //  Get all posts
                .antMatchers(HttpMethod.GET, "/rest/post/*").permitAll()  //  Get one post

                //TODO: Delete later!
                .antMatchers(HttpMethod.GET, "/rest/resource").authenticated() // Delete later!

                // This door is locked ! Level 20 door needs lvl. 5 Pick Locks skill
                .anyRequest().denyAll()

                .and()

                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}
