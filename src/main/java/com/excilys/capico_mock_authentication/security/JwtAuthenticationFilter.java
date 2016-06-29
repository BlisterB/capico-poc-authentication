package com.excilys.capico_mock_authentication.security;

import com.excilys.capico_mock_authentication.exception.JwtTokenMissingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The entry point of our JWT Authentication protected pages
 */
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final String HEADER_NAME = "Authorization", HEADER_START = "Bearer ";

    @Autowired
    private JwtUtil jwtUtil;

    public JwtAuthenticationFilter() {
        super("/**");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // Extracts the JWT token from the request headers
        String header = request.getHeader(HEADER_NAME);
        if (header == null || !header.startsWith(HEADER_START)) {
            throw new JwtTokenMissingException("No JWT token found in request headers");
        }
        String authToken = header.substring(HEADER_START.length());

        // Parse and return the token
        return  jwtUtil.parseToken(authToken);

        /* If we want spring security to always check the user's validity in DB:
                return getAuthenticationManager().authenticate(authRequest); */
    }

    /**
     * We need to override successfulAuthentication because the default Spring flow would stop the filter
     * chain and proceed with a redirect. We need the chain to execute fully (including generating the response).
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);

        // As this authentication is in HTTP header, after success we need to continue the request normally
        // and return the response as if the resource was not secured at all
        chain.doFilter(request, response);
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return true;
    }
}