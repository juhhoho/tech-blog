package com.blog.auth.jwt;

import com.blog.auth.dto.SocialUserDetails;
import com.blog.auth.entity.Refresh;
import com.blog.auth.repository.refresh.RefreshRepository;
import com.blog.util.CookieUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Component
public class SocialUserLoginHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public SocialUserLoginHandler(JWTUtil jwtUtil, RefreshRepository refreshRepository) {

        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
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

        // 토큰 생성
        String access = jwtUtil.createJwt("access", identifier, role, 600000L);
        String refresh = jwtUtil.createJwt("refresh", identifier, role, 86400000L);

        // access: 헤더, refresh: 토큰
        response.setHeader("access", access);
        response.addCookie(CookieUtils.createCookie("refresh", refresh));

        // Refresh rotate
        Refresh refreshRotate = Refresh.builder()
                .username(identifier)
                .refresh(refresh)
                .expiration(new Date(System.currentTimeMillis() + 86400000L).toString())
                .build();

        refreshRepository.save(refreshRotate);
    }
}