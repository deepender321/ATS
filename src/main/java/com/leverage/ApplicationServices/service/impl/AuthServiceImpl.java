package com.leverage.ApplicationServices.service.impl;

import com.leverage.ApplicationServices.DTO.AuthRequestDto;
import com.leverage.ApplicationServices.DTO.AuthResponseDto;
import com.leverage.ApplicationServices.configuration.jwtConfig.JwtTokenGenerator;
import com.leverage.ApplicationServices.enums.TokenType;
import com.leverage.ApplicationServices.model.User;
import com.leverage.ApplicationServices.repo.UserRepo;
import com.leverage.ApplicationServices.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepo;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepo userRepo,
                           JwtTokenGenerator jwtTokenGenerator,
                           PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Object authenticate(AuthRequestDto authRequestDto) {
        try {
            User user = userRepo.findByMail(authRequestDto.getMailId())
                    .orElseThrow(() -> {
                        log.error("[AuthService:userSignInAuth] User :{} not found", authRequestDto.getMailId());
                        return new ResponseStatusException(HttpStatus.NOT_FOUND, "USER NOT FOUND");
                    });

            //  Diagnostic logs
            System.out.println(" Encoder class: " + passwordEncoder.getClass().getName());
            System.out.println(" Incoming email: " + authRequestDto.getMailId());
            System.out.println(" Incoming raw password: " + authRequestDto.getPassword());
            System.out.println(" Stored DB password: " + user.getPassword());
            System.out.println(" Match result: " + passwordEncoder.matches(authRequestDto.getPassword(), user.getPassword()));

            // Password check
            if (!passwordEncoder.matches(authRequestDto.getPassword(), user.getPassword())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect username or password");
            }

            String accessToken = jwtTokenGenerator.generateAccessToken(user);

            log.info("[AuthService:userSignInAuth] Access token for user : {}, has been generated",
                    user.getFirstName() + " at time " + Instant.now());

            return AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(String.valueOf(15 * 60) + " secs")
                    .userName(user.getFirstName() + " " + user.getLastName())
                    .userRole(String.valueOf(user.getRoles()))
                    .tokenType(TokenType.BEARER)
                    .build();

        } catch (Exception e) {
            log.error("[AuthService:userSignInAuth] Exception while authenticating the user due to :" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Please Try Again");
        }
    }
}
