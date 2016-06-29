package com.excilys.capico_mock_authentication.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {
    public JwtAuthenticationToken(String username, Collection<GrantedAuthority> authorities) {
        super(username, null, authorities);
    }
}
