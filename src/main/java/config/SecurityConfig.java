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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    AuthenticationManager auth;

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration conf) throws Exception{
        auth = conf.getAuthenticationManager();
        return auth;
    }

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
                .logoutUrl("/logout") // Configura el endpoint para el logout
                .deleteCookies("JSESSIONID") // Elimina la cookie de sesión al hacer el logout
                .invalidateHttpSession(true) // Invalida la sesión del usuario al hacer el logout
                .clearAuthentication(true) // Borra la autenticación del usuario al hacer el logout
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(HttpServletResponse.SC_OK);
                }) // Manejador personalizado para la respuesta del logout
                .permitAll() // Permite a cualquier usuario acceder al endpoint de logout
                .and()
                .addFilter(new JWTAuthorizationFilter(auth));
        return http.build();
    }
}
