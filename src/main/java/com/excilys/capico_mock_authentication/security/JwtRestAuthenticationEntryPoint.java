package com.excilys.capico_mock_authentication.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Invoked when user tries to access a secured REST resource without supplying any credentials.
 * Send a 401 Unauthorized response because there is no 'login page' to redirect to in a REST Service.
 * Internet Browser will ask users for credential information.
 */
public class JwtRestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}