package com.excilys.capico_mock_authentication.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtExpiredException extends AuthenticationException {
    public JwtExpiredException(String msg) {
        super(msg);
    }
}
