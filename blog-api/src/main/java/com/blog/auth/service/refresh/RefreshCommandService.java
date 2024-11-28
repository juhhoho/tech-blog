package com.blog.auth.service.refresh;

import com.blog.exception.CustomException.JwtException;
import com.blog.exception.CustomException.NoResourceFoundException;
import com.blog.auth.dto.response.ReissueResponse;
import com.blog.auth.jwt.JWTUtil;
import com.blog.util.CookieUtils;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshCommandService {

    private final JWTUtil jwtUtil;
    private final StringRedisTemplate stringRedisTemplate;

    public ResponseEntity<ReissueResponse> reissue(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("[RefreshCommandService - reissue]");

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
        String role = jwtUtil.getRole(oldRefreshToken);
        String redisKeyPattern = "refresh.identifier." + identifier;
        String redisStoredRefreshToken = stringRedisTemplate.opsForValue().get(redisKeyPattern);


        // oldRefresh가 유효해야만 그를 바탕으로 access, refresh 다시 만듦
        if (!isValidRefreshToken(oldRefreshToken, redisStoredRefreshToken, response)) {
            throw new RuntimeException("refresh token이 유효하지 않습니다.");
        }

        // oldRefresh가 적절하다면 기존에 redis에 저장된 oldRefresh 삭제
        stringRedisTemplate.delete(redisKeyPattern);

        // access, refresh 모두 재발급
        String newAccessToken = jwtUtil.createJwt("access", identifier, role, 600000L);
        String newRefreshToken = jwtUtil.createJwt("refresh", identifier, role, 86400000L);

        // redis -> newRefresh 저장(24시간), 즉 24시간 동안 로그인 유지
        stringRedisTemplate.opsForValue().set("refresh.identifier." + identifier,  newRefreshToken, 24 , TimeUnit.HOURS);

        // 새롭게 발급한 토큰 전송
        response.setHeader("access", newAccessToken);
        response.addCookie(CookieUtils.createCookie("refresh", newRefreshToken));

        return ResponseEntity
                .ok()
                .body(ReissueResponse.builder().newAccessToken(newAccessToken).build());
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
