package com.excilys.capico_mock_authentication.controller;

import com.excilys.capico_mock_authentication.security.JwtUtil;
import com.excilys.capico_mock_authentication.service.BlackListedTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class Logout {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BlackListedTokenService blackListedTokenService;

    @RequestMapping(value = "/api/logout", method = POST)
    public ResponseEntity<?> logout(HttpServletRequest request) {
        // Fetch the JSON Web Token in the header packet and place it in a black list
        String jwtToken = jwtUtil.extractStringTokenFromRequest(request);
        blackListedTokenService.blackList(jwtToken);

        return ResponseEntity.ok().body(null);
    }
}