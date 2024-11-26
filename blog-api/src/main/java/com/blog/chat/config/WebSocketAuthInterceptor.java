package com.blog.chat.config;

import com.blog.auth.entity.Refresh;
import com.blog.auth.jwt.JWTUtil;
import com.blog.auth.repository.refresh.RefreshCustomRepository;
import com.blog.auth.repository.refresh.RefreshRepository;
import com.blog.util.CookieUtils;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final RefreshCustomRepository refreshCustomRepository;

    /**
     * 웹소켓 연결 전 인터셉터
     * access 헤더를 확인하여 유저를 인증한다.
     * 유저 명이 채팅유저로 시작하지 않으면 401을 반환한다.
     * 유저 명이 채팅유저로 시작해서 인증된 유저라면 session에 identifier를 저장한다.
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        log.info("[beforeHandshake]");
        if (request instanceof ServletServerHttpRequest) {
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();

            String accessToken = servletRequest.getHeader("access");
            if (accessToken == null) {
                sendErrorResponse(servletResponse, "Access token이 없습니다.", 401);
                return false;
            }

            try {
                jwtUtil.isExpired(accessToken);
            } catch (ExpiredJwtException e) {
                String oldRefreshToken = getRefreshTokenFromCookies(servletRequest);
                if (oldRefreshToken == null || !isValidRefreshToken(oldRefreshToken, servletResponse)) {
                    return false;
                }

                // 재발급
                String identifier = jwtUtil.getUsername(oldRefreshToken);
                String role = jwtUtil.getRole(oldRefreshToken);
                String newAccessToken = jwtUtil.createJwt("access", identifier, role, 600000L);
                String newRefreshToken = jwtUtil.createJwt("refresh", identifier, role, 86400000L);

                refreshCustomRepository.deleteByRefresh(oldRefreshToken);

                Refresh refreshRotate = Refresh.builder()
                        .username(identifier)
                        .refresh(newRefreshToken)
                        .expiration(new Date(System.currentTimeMillis() + 86400000L).toString())
                        .build();
                refreshRepository.save(refreshRotate);

                servletResponse.setHeader("access", newAccessToken);
                servletResponse.addCookie(CookieUtils.createCookie("refresh", newRefreshToken));

                attributes.put("identifier", identifier);
                return true;
            }

            String identifier = jwtUtil.getUsername(accessToken);
            attributes.put("identifier", identifier);
            return true;
        }
        return false;
    }

    /**
     * 쿠키에서 Refresh Token 추출
     */
    private String getRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }
    /**
     * Refresh Token의 유효성 검증
     */
    private boolean isValidRefreshToken(String refreshToken, HttpServletResponse response) throws IOException {
        try {
            jwtUtil.isExpired(refreshToken);

            String category = jwtUtil.getCategory(refreshToken);
            if (!"refresh".equals(category)) {
                sendErrorResponse(response, "유효하지 않은 Refresh Token입니다.", 401);
                return false;
            }

            if (!refreshRepository.existsByRefresh(refreshToken)) {
                sendErrorResponse(response, "Refresh Token이 서버에 존재하지 않습니다.", 401);
                return false;
            }

            return true;
        } catch (ExpiredJwtException e) {
            sendErrorResponse(response, "Refresh Token이 만료되었습니다.", 401);
            return false;
        }
    }

    /**
     * 에러 응답 설정
     */
    private void sendErrorResponse(HttpServletResponse response, String message, int status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.write("{\"error\":\"" + message + "\"}");
        writer.flush();
    }

}
