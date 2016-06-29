package com.excilys.capico_mock_authentication.security;

import com.excilys.capico_mock_authentication.exception.JwtTokenMalformedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

@Component
public class JwtUtil {
    // TODO : Take the secret from a file, and use : @Value("${jwt.secret}")
    private static String secret = "capico4evur";

    private static final String TAG_AUTHORITIES = "r";
    private final static int TOKEN_DURATION_IN_HOUR = 3;

    /**
     * Generates a JWT token containing username as subject, and role and expiration date as additional claims.
     * @param u the user for which the token will be generated
     * @return the JWT token
     */
    public String generateToken(User u) {
        Claims claims = Jwts.claims().setSubject(u.getUsername());

        // Put properties into the token
        claims.put(TAG_AUTHORITIES, u.getAuthorities().toString());

        // Define the expiration date
        Calendar cal = Calendar.getInstance(); // The current date
        cal.add(Calendar.HOUR, TOKEN_DURATION_IN_HOUR);
        Date expirationDate = cal.getTime();

        // Sign and return the token
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * Tries to parse specified String as a JWT token.
     * If successful, returns User object with username and roles filled.
     *
     * @param token the JWT token to parse
     * @return the User object extracted from specified token or null if a token is invalid.
     */
    public JwtAuthenticationToken parseToken(String token) throws JwtTokenMalformedException, ExpiredJwtException{
        // Parsing : it can throw an expiredJwtException if the token is expired
        Claims body = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        // Extract properties
        String username = body.getSubject();
        if (username == null || username.isEmpty()) {
            throw new JwtTokenMalformedException("No username found in the JSON Web Token");
        }

        Collection<GrantedAuthority> authorities =  AuthorityUtils.commaSeparatedStringToAuthorityList((String) body.get(TAG_AUTHORITIES));
        if (authorities == null || authorities.isEmpty()) {
            throw new JwtTokenMalformedException("No authorities found in the JSON Web Token");
        }

        return new JwtAuthenticationToken(username, authorities);
    }
}