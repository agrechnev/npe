package agrechnev.security;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * Created by Oleksiy Grechnyev on 12/17/2016.
 */
@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()

                .and()

                .authorizeRequests()
                // Unlock static pages
//                .antMatchers("/index.html", "/home.html", "/login.html",
//                        "/", "/red.html", "/green.html").permitAll()
                .antMatchers("/*.html", "/", "/ajs/*.js", "/user", "/user/").permitAll()
                .anyRequest().authenticated()

                .and()

                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }
}
