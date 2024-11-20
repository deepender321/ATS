package com.leverage.ApplicationServices.configuration.jwtConfig;


import com.leverage.ApplicationServices.configuration.userConfig.UserInfoConfig;
import com.leverage.ApplicationServices.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtTokenGenerator {

    private final JwtEncoder jwtEncoder;

    public String generateAccessToken(User user) {

        log.info("[JwtTokenGenerator:generateAccessToken] Token Creation Started for:{}", user.getFirstName());

        log.info("User info : " + user);

        UserInfoConfig userInfoConfig = new UserInfoConfig(user);
        String roles = getRolesOfUser2(userInfoConfig);

        log.info("Role for the user {}", userInfoConfig.getUsername() + " is : " + roles);

//        String permissions = getPermissionsFromRoles(roles);

//        log.info("Permission for the user {} is: {}", userInfoConfig.getUsername(), permissions);

        // Set roles in the claims prefixed with "ROLE_"
        List<String> rolesList = Arrays.stream(roles.split(" "))
                .map(role -> "ROLE_" + role) // Prefix roles with "ROLE_"
                .collect(Collectors.toList());
        log.info("Role for the user {}", userInfoConfig.getUsername() + " is : " + rolesList);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("Levarage")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(60, ChronoUnit.HOURS))
                .subject(user.getMail())
                .claim("roles", rolesList)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }


    private static String getRolesOfUser2(UserInfoConfig userInfoConfig) {
        return userInfoConfig.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
    }



//    Below method is for if what to restrict access using role permissions
//    private String getPermissionsFromRoles(String roles) {
//        Set<String> permissions = new HashSet<>();
//
//        if (roles.contains("ADMIN")) {
//            permissions.addAll(List.of("READ", "WRITE", "DELETE"));
//        }
//        if (roles.contains("CANDIDATE")) {
//            permissions.add("READ");
//        }
//        if (roles.contains("MARKETING_MEMBER")) {
//            permissions.add("READ");
//        }
//
//        return String.join(" ", permissions);
//    }


}
