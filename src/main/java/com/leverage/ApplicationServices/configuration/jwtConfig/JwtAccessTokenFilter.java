package com.leverage.ApplicationServices.configuration.jwtConfig;


import com.leverage.ApplicationServices.configuration.RSAKeyRecord;
import com.leverage.ApplicationServices.enums.TokenType;
import com.leverage.ApplicationServices.model.User;
import com.leverage.ApplicationServices.repo.UserRepo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
public class JwtAccessTokenFilter extends OncePerRequestFilter {

    private final RSAKeyRecord rsaKeyRecord;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserRepo userRepo;

    public JwtAccessTokenFilter(RSAKeyRecord rsaKeyRecord, JwtTokenUtils jwtTokenUtils, UserRepo userRepo) {
        this.rsaKeyRecord = rsaKeyRecord;
        this.jwtTokenUtils = jwtTokenUtils;
        this.userRepo = userRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            log.info("[JwtAccessTokenFilter:doFilterInternal] :: Started ");

            log.info("[JwtAccessTokenFilter:doFilterInternal] Filtering the Http Request: {}", request.getRequestURI());

            final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith(TokenType.BEARER.name())) {
                filterChain.doFilter(request, response);
                return;
            }

            final String token = authHeader.substring(7);
            JwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(rsaKeyRecord.rsaPublicKey()).build();
            final Jwt jwtToken = jwtDecoder.decode(token);

            log.info("Decoded JWT Claims: {}", jwtToken.getClaims());

            final String userName = jwtTokenUtils.getUserName(jwtToken);

            log.info("Username of token is : {}", userName);

//            User user = userRepo.findByUserName(userName);

            Optional<User> user = userRepo.findByMail(userName);

            if (user.isEmpty()) {
                log.warn("User not found with username: {}", userName);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
                return;
            }

            if (!userName.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
                log.info("Found user with emailId: {}", user.get().getMail());
                UserDetails userDetails = jwtTokenUtils.userDetails(user.get().getMail());


                // Extract roles from the token claims
                List<String> roles = jwtToken.getClaimAsStringList("roles");
                Collection<GrantedAuthority> authorities = roles.stream()
                        .map(role -> new SimpleGrantedAuthority(role)) // Use roles directly
                        .collect(Collectors.toList());


                if (jwtTokenUtils.isTokenValid(jwtToken, userDetails)) {
                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                    UsernamePasswordAuthenticationToken createdToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            authorities
                    );
                    createdToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    securityContext.setAuthentication(createdToken);
                    SecurityContextHolder.setContext(securityContext);
                }
            }

            log.info("[JwtAccessTokenFilter:doFilterInternal] Completed");
            filterChain.doFilter(request, response);
        } catch (JwtValidationException jwtValidationException) {
            log.error("[JwtAccessTokenFilter:doFilterInternal] JWT Validation Error: {}", jwtValidationException.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT validation failed: " + jwtValidationException.getMessage());
        } catch (AccessDeniedException accessDeniedException) {
            log.error("[JwtAccessTokenFilter:doFilterInternal] Access denied: {}", accessDeniedException.getMessage());
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have permission to access this resource.");
        }
    }

}
