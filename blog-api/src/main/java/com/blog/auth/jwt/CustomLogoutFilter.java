package com.blog.auth.jwt;

import com.blog.auth.repository.BaseUser.BaseUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;
    private final BaseUserRepository baseUserRepository;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }


    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        //path and method verify
        String requestUri = request.getRequestURI();
        if (!requestUri.matches("^\\/logout$")) {
            System.out.println("requestUri [" + requestUri + "] is not logout");
            filterChain.doFilter(request, response);
            return;
        }
        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {

            filterChain.doFilter(request, response);
            return;
        }

        log.info("[CustomLogoutFilter]");
        //get refresh token
        String oldRefreshToken = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                oldRefreshToken = cookie.getValue();
            }
        }

        // oldRefresh 토큰의 유효성 검증을 위해 필요한 데이터 가공
        String identifier = jwtUtil.getUsername(oldRefreshToken);
        String redisKeyPattern = "refresh.identifier." + identifier;
        String redisStoredRefreshToken = stringRedisTemplate.opsForValue().get(redisKeyPattern);


        // oldRefresh가 유효해야만 그를 바탕으로 access, refresh 다시 만듦
        if (!isValidRefreshToken(oldRefreshToken, redisStoredRefreshToken, response)) {
            throw new RuntimeException("refresh token이 유효하지 않습니다.");
        }

        // oldRefresh가 적절하다면 기존에 redis에 저장된 oldRefresh 삭제
        stringRedisTemplate.delete(redisKeyPattern);

        //Refresh 토큰 Cookie 값 0
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"" +  baseUserRepository.findByIdentifier(identifier).getId() + " logout complete!" + "\"}");
    }

    private boolean isValidRefreshToken(String oldRefreshToken, String redisStoredRefreshToken, HttpServletResponse response) throws IOException {
        // (1) token 값이 없음
        if(oldRefreshToken == null){
            return false;
        }
        // (2) 토큰 만료
        jwtUtil.isExpired(oldRefreshToken);

        // (3) refresh 토큰이 아님
        String category = jwtUtil.getCategory(oldRefreshToken);
        if (!"refresh".equals(category)) {
            return false;
        }

        // (4) 같은 id에서 파생된 서버에 저장된 redisStoredRefreshToken와 oldRefreshToken 가 다른 토큰임.
        if(!redisStoredRefreshToken.equals(redisStoredRefreshToken)){
            return  false;
        }

        return true;
    }
}


