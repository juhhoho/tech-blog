package com.blog.oauth2.jwt;

import com.blog.oauth2.dto.LocalUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class LocalUserLoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public LocalUserLoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("[LocalUserLoginFilter - attemptAuthentication]");

        String identifier = obtainUsername(request);
        String password = obtainPassword(request);

        System.out.println("identifier = " + identifier);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(identifier, password);

        return authenticationManager.authenticate(authToken);

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        log.info("[LocalUserLoginFilter - successfulAuthentication]");
        Object principal = authentication.getPrincipal();

        String identifier = extractIdentifier(principal);
        String role = extractRole(principal, authentication);

        String token = jwtUtil.createJwt(identifier, role, 60*60*10L);

        response.addHeader("Authorization", "Bearer " + token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        log.info("[LocalUserLoginFilter - unsuccessfulAuthentication]");
        response.setStatus(401);
    }

    private String extractIdentifier(Object principal) {
        if (principal instanceof LocalUserDetails) {
            return ((LocalUserDetails) principal).getUsername();
        }
        throw new IllegalStateException("Unexpected user principal type: " + principal.getClass().getName());
    }

    private String extractRole(Object principal, Authentication authentication) {
        if (principal instanceof LocalUserDetails) {
            return authentication.getAuthorities().iterator().next().getAuthority();
        }
        throw new IllegalStateException("Unexpected user principal type: " + principal.getClass().getName());
    }
}
