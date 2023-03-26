package controllers;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.stream.Collectors;

import static util.Constants.KEY;
import static util.Constants.LIFE_CYCLE;

/**
 * This controller handles user authentication and generates JWT tokens for authenticated users.
 */
@RestController
@CrossOrigin(origins = {"*"})
public class AuthController {

    private AuthenticationManager authManager;

    /**
     * Creates a new AuthController instance with the given authentication manager.
     * @param authManager the authentication manager to use for user authentication
     */
    public AuthController(AuthenticationManager authManager){
        this.authManager = authManager;
    }

    /**
     * Authenticates the user with the given username and password and generates a JWT token if successful.
     * @param user the username of the user to authenticate
     * @param pwd the password of the user to authenticate
     * @return a JWT token if authentication is successful, "don't authenticated" otherwise
     */
    @PostMapping("login")
    public String login(@RequestParam("user") String user, @RequestParam("pwd") String pwd){
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user, pwd));
        //if the user is authenticated, generates the token
        if (authentication.isAuthenticated()){
            return getToken(authentication);
        }else{
            return "don't authenticated";
        }
    }

    /**
     * Generates a JWT token for the given authenticated user.
     * @param authentication the authentication object containing the user's identity and authorities
     * @return a JWT token for the authenticated user
     */
    private String getToken(Authentication authentication){
        // The user and its roles are included in the token's body, besides the expiration date and the signature data
        String token = Jwts.builder()
                .setIssuedAt(new Date()) //Creation date
                .setSubject(authentication.getName()) //user
                .claim("authorities", authentication.getAuthorities().stream()//roles
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .setExpiration(new Date(System.currentTimeMillis() + LIFE_CYCLE)) //Expiration date
                .signWith(SignatureAlgorithm.HS512, KEY)
                .compact(); //Key and algorithm for the signature
        return token;
    }
}
