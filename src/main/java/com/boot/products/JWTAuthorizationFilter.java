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

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public JWTAuthorizationFilter(AuthenticationManager auth){
        super(auth);
    }

    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException{
        String header = req.getHeader(ENCABEZADO);
        if (header == null || !header.startsWith(PREFIJO_TOKEN)){
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

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request){
        //the token is in the request header
        String token = request.getHeader(ENCABEZADO);
        if (token != null){
            //The token is processed and the user and the roles are retrieved
            Claims claims = Jwts.parser()
                    .setSigningKey(CLAVE)
                    .parseClaimsJws(token.replace(PREFIJO_TOKEN, ""))
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
