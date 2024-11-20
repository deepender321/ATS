package com.leverage.ApplicationServices.controller;

import com.leverage.ApplicationServices.DTO.AuthRequestDto;
import com.leverage.ApplicationServices.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthenticationController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationController(AuthService authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping()
    public ResponseEntity<?> signIn(@RequestBody AuthRequestDto authRequestDto){
        log.info("Attempting to sign in user with email: {}", authRequestDto.getMailId());
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDto.getMailId(), authRequestDto.getPassword()));
            log.info("Authentication successful for user: {}", authRequestDto.getMailId());
        } catch (BadCredentialsException e) {
            log.error("Authentication failed: {}", e.getMessage());
            throw new BadCredentialsException("Incorrect username or password");
        }
        return ResponseEntity.ok(authService.authenticate(authRequestDto));
    }
}
