package com.blog.chat.config;

import com.blog.auth.jwt.JWTUtil;
import com.blog.util.CookieUtils;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    private final JWTUtil jwtUtil;
    private final StringRedisTemplate stringRedisTemplate;
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
                // access 토큰이 만료 되지 않음
                jwtUtil.isExpired(accessToken);
            } catch (ExpiredJwtException e) {

                // access 토큰이 만료됨
                String oldRefreshToken = getRefreshTokenFromCookies(servletRequest);

                // oldRefresh 토큰의 유효성 검증을 위해 필요한 데이터 가공
                String identifier = jwtUtil.getUsername(oldRefreshToken);
                String role = jwtUtil.getRole(oldRefreshToken);
                String redisKeyPattern = "refresh.identifier." + identifier;
                String redisStoredRefreshToken = stringRedisTemplate.opsForValue().get(redisKeyPattern);

                // oldRefresh가 유효해야만 그를 바탕으로 access, refresh 다시 만듦
                if (!isValidRefreshToken(oldRefreshToken, redisStoredRefreshToken, servletResponse)) {
                    return false;
                }

                // oldRefresh가 적절하다면 기존에 redis에 저장된 oldRefresh 삭제
                stringRedisTemplate.delete(redisKeyPattern);

                // access, refresh 모두 재발급
                String newAccessToken = jwtUtil.createJwt("access", identifier, role, 600000L);
                String newRefreshToken = jwtUtil.createJwt("refresh", identifier, role, 86400000L);

                // redis -> newRefresh 저장(24시간), 즉 24시간 동안 로그인 유지
                stringRedisTemplate.opsForValue().set("refresh.identifier." + identifier,  newRefreshToken, 24 , TimeUnit.HOURS);

                // 새롭게 발급한 토큰 전송
                servletResponse.setHeader("access", newAccessToken);
                servletResponse.addCookie(CookieUtils.createCookie("refresh", newRefreshToken));

                attributes.put("identifier", identifier);
                return true;
            }

            String identifier = jwtUtil.getUsername(accessToken);

            // ws 통신에서 사용할 session에 identifier 값을 넣어줌.
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
    private boolean isValidRefreshToken(String oldRefreshToken, String redisStoredRefreshToken, HttpServletResponse response) throws IOException {
        try {
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
