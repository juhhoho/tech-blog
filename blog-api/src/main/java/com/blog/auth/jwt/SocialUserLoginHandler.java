package com.blog.auth.jwt;

import com.blog.auth.dto.SocialUserDetails;
import com.blog.util.CookieUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class SocialUserLoginHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final StringRedisTemplate stringRedisTemplate;

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
        String newAccess = jwtUtil.createJwt("access", identifier, role, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", identifier, role, 86400000L);

        // access: 헤더, refresh: 토큰
        response.setHeader("access", newAccess);
        response.addCookie(CookieUtils.createCookie("refresh", newRefresh));

        // redis -> newRefresh 저장(24시간), 즉 24시간 동안 로그인 유지
        stringRedisTemplate.opsForValue().set("refresh:identifier:" + identifier,  newRefresh, 24 , TimeUnit.HOURS);

    }
}