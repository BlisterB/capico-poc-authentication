package com.excilys.capico_mock_authentication.security;

import com.excilys.capico_mock_authentication.exception.JwtExpiredException;
import com.excilys.capico_mock_authentication.exception.JwtTokenMalformedException;
import com.excilys.capico_mock_authentication.service.BlackListedTokenService;
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

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private BlackListedTokenService blackListedTokenService;

    public JwtAuthenticationFilter() {
        super("/**");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String authToken = jwtUtil.extractStringTokenFromRequest(request);

        // Verify that the token is not in the black list (after a blackList)
        if (blackListedTokenService.isBlackListed(authToken)) {
            throw new JwtExpiredException("The token has expired");
        }

        // Parse and return the token
        try {
            return jwtUtil.parseToken(authToken);
        } catch (Exception e) {
            throw new JwtTokenMalformedException(e.getMessage());
        }
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