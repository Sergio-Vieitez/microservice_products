package com.boot.products;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static util.Constants.*;


/**
 * This class implements a JWT authorization filter, which extends BasicAuthenticationFilter class from Spring Security.
 * It retrieves the JWT token from the request header and processes it to obtain the user information and roles.
 * The user information is then saved in the security context for Spring Security to use during the authorization process.
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    /**
     * Constructs a JWTAuthorizationFilter object with the specified AuthenticationManager.
     * @param auth The AuthenticationManager used to authenticate the user.
     */
    public JWTAuthorizationFilter(AuthenticationManager auth){
        super(auth);
    }

    /**
     * This method filters the incoming request to retrieve the JWT token from the header and process it
     * to obtain the user information and roles. The user information is then saved in the security context for
     * Spring Security to use during the authorization process.
     * @param req The HttpServletRequest object representing the incoming request.
     * @param res The HttpServletResponse object representing the response to be sent.
     * @param chain The FilterChain object representing the chain of filters to be applied to the request.
     */
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException{
        String header = req.getHeader(HEADER);
        if (header == null || !header.startsWith(TOKEN_PREFIX)){
            chain.doFilter(req, res);
            return;
        }
        //Obtain user data from the token
        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
        //Save the user information in the security context for it to be able to be used
        //by spring security during the authorization process
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    /**
     * This method processes the JWT token to obtain the user information and roles.
     * The user information is then used to create a UsernamePasswordAuthenticationToken object,
     * which is returned.
     * @param request The HttpServletRequest object representing the incoming request.
     * @return The UsernamePasswordAuthenticationToken object containing the user information.
     */
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request){
        //the token is in the request header
        String token = request.getHeader(HEADER);
        if (token != null){
            //The token is processed and the user and the roles are retrieved
            Claims claims = Jwts.parser()
                    .setSigningKey(KEY)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody();
            String user = claims.getSubject();
            List<String> authorities = (List<String>) claims.get("authorities");
            if (user != null){
                //Create object with user information
                return new UsernamePasswordAuthenticationToken(user, null, authorities.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList()));
            }
            return null;
        }
        return null;
    }
}
