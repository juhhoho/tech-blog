package com.blog.oauth2.jwt;

import com.blog.oauth2.dto.SocialUserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
public class SocialUserLoginHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    public SocialUserLoginHandler(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        SocialUserDetails socialUserDetails = (SocialUserDetails) authentication.getPrincipal();

        // userDto.username 반환(userDto.username == identifier)
        String identifier = socialUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();


        String token = jwtUtil.createJwt(identifier, role, 60*60*1000L);

        response.addHeader("Authorization", "Bearer " + token);
    }
}