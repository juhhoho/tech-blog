package com.blog.auth.jwt;

import com.blog.auth.entity.BaseUser;
import com.blog.auth.entity.Refresh;
import com.blog.auth.repository.user.BaseUserRepository;
import com.blog.exception.CustomException.JwtException;
import com.blog.exception.CustomException.NoResourceFoundException;
import com.blog.auth.repository.refresh.RefreshCustomRepository;
import com.blog.auth.repository.refresh.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final RefreshCustomRepository refreshCustomRepository;
    private final BaseUserRepository baseUserRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }


    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        log.info("[CustomLogoutFilter]");
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

        System.out.println("asdasdasdasd");

        //get refresh token
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refreshToken = cookie.getValue();
            }
        }

        //refresh null check
        if (refreshToken == null) {
            throw new JwtException("브라우저 캐시(쿠키)에 저장된 refresh token을 찾을 수 없습니다.");
        }

        //expired check
        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new JwtException("브라우저 캐시(쿠키)에 저장된 refresh token의 기한이 만료되었습니다.");
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refreshToken);
        if (!category.equals("refresh")) {
            throw new JwtException("token의 타입이 refresh token이 아닙니다.");
        }

        //DB에 저장되어 있는지 확인
        if (!refreshRepository.existsByRefresh(refreshToken)) {
            //response body
            throw new NoResourceFoundException(refreshToken + "를 refresh로 갖는 토큰을 찾을 수 없습니다.");
        }

        //로그아웃 진행
        Refresh refresh = refreshRepository.findByRefresh(refreshToken).orElseThrow(
                () -> new NoResourceFoundException("refresh 토큰을 찾을 수 없습니다.")
        );

        //Refresh 토큰 DB에서 제거
        refreshCustomRepository.deleteByRefresh(refreshToken);

        //Refresh 토큰 Cookie 값 0
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"" +  baseUserRepository.findByIdentifier(refresh.getUsername()).getId() + " logout complete!" + "\"}");
    }
}
