package com.hybrid.libraryproject.controller;

import com.hybrid.libraryproject.model.User;
import com.hybrid.libraryproject.security.AuthenticationRequest;
import com.hybrid.libraryproject.security.AuthenticationResponse;
import com.hybrid.libraryproject.security.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(value = "/api/auth")
@Slf4j
public class AuthenticationController {

    private final TokenUtils tokenUtils;

    private final AuthenticationManager authenticationManager;

    public AuthenticationController(TokenUtils tokenUtils, AuthenticationManager authenticationManager){
        this.tokenUtils = tokenUtils;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest,
                                                                            HttpServletResponse response) throws AuthenticationException, IOException {

        final Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(user.getEmail());
        int expiresIn = tokenUtils.getExpiredIn();

        return ResponseEntity.ok(new AuthenticationResponse(jwt, expiresIn));
    }
}
