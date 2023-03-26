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

import static util.Constants.CLAVE;
import static util.Constants.TIEMPO_VIDA;

@RestController
@CrossOrigin(origins = {"*"})
public class AuthController {

    private AuthenticationManager authManager;

    public AuthController(AuthenticationManager authManager){
        this.authManager = authManager;
    }

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

    private String getToken(Authentication authentication){
        // The user and its roles are included in the token's body, besides the expiration date and the signature data
        String token = Jwts.builder()
                .setIssuedAt(new Date()) //Creation date
                .setSubject(authentication.getName()) //user
                .claim("authorities", authentication.getAuthorities().stream()//roles
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .setExpiration(new Date(System.currentTimeMillis() + TIEMPO_VIDA)) //Expiration date
                .signWith(SignatureAlgorithm.HS512, CLAVE)
                .compact(); //Key and algorithm for the signature
        return token;
    }
}
