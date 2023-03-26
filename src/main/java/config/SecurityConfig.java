package config;

import com.boot.products.JWTAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 This class defines the security configuration for the application.
 It enables web security and configures the authentication manager and user details manager.
 It also defines the security filter chain and sets up the authorization rules for different HTTP methods and endpoints.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    AuthenticationManager auth;

    /**
     Creates and returns an authentication manager by using the provided authentication configuration.
     @param conf the authentication configuration
     @return the authentication manager
     @throws Exception if there is an error creating the authentication manager
     */
    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration conf) throws Exception{
        auth = conf.getAuthenticationManager();
        return auth;
    }

    /**
     Creates and returns an in-memory user details manager.
     The manager contains a list of users with different authorities.
     @return the in-memory user details manager
     @throws Exception if there is an error creating the user details manager
     */
    @Bean
    protected InMemoryUserDetailsManager detailsManager() throws Exception{
        List<UserDetails> users = List.of(
                User.withUsername("user1")
                        .password("{noop}user1")
                        .authorities("USERS")
                        .build(),
                User.withUsername("admin")
                        .password("{noop}admin")
                        .authorities("USERS", "ADMIN")
                        .build()
        );
        return new InMemoryUserDetailsManager(users);
    }

    /**
     Creates and returns the security filter chain.
     It configures the HTTP security and sets up the authorization rules for different HTTP methods and endpoints.
     It also adds a custom JWT authorization filter to the filter chain.
     @param http the HttpSecurity object
     @return the security filter chain
     @throws Exception if there is an error creating the filter chain
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/addproduct").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/updateproduct").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/deleteproduct").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/products").hasAnyAuthority("ADMIN", "USERS")
                .antMatchers(HttpMethod.GET, "/productdetail").hasAnyAuthority("ADMIN", "USERS")
                .and()
                .logout()
                .logoutUrl("/logout") // Configures the endpoint for the logout
                .deleteCookies("JSESSIONID") // Deletes the session cookie when logging out
                .invalidateHttpSession(true) // Invalidates the user's session when logging out
                .clearAuthentication(true) // Delete the user's authentication when logging out
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(HttpServletResponse.SC_OK);
                }) // Custom handler for the logout response
                .permitAll() // Permits any user to access the logout endpoint
                .and()
                .addFilter(new JWTAuthorizationFilter(auth));
        return http.build();
    }
}
