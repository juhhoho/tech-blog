package com.blog.auth.service.refresh;

import com.blog.exception.CustomException.JwtException;
import com.blog.exception.CustomException.NoResourceFoundException;
import com.blog.auth.dto.response.ReissueResponse;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshCommandService {

    private final RefreshCustomRepository refreshCustomRepository;
    private final RefreshRepository refreshRepository;
    private final JWTUtil jwtUtil;

    public ResponseEntity<ReissueResponse> reissue(HttpServletRequest request, HttpServletResponse response) {
        log.info("[RefreshCommandService - reissue]");

        //get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }
        //refresh null check
        if (refresh == null) {
            throw new JwtException("브라우저 캐시(쿠키)에 저장된 refresh token을 찾을 수 없습니다.");
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            throw new JwtException("브라우저 캐시(쿠키)에 저장된 refresh token의 기한이 만료되었습니다.");
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            throw new JwtException("token의 타입이 refresh token이 아닙니다.");
        }

        //DB에 저장되어 있는지 확인
        if (!refreshRepository.existsByRefresh(refresh)) {
            //response body
            throw new NoResourceFoundException(refresh + "를 refresh로 갖는 토큰을 찾을 수 없습니다.");
        }

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        //make new JWT
        String newAccess = jwtUtil.createJwt("access", username, role, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", username, role, 86400000L);

        //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제
        refreshCustomRepository.deleteByRefresh(refresh);

        // save refresh for rotate
        Refresh refreshRotate = Refresh.builder()
                .username(username)
                .refresh(refresh)
                .expiration(new Date(System.currentTimeMillis() + 86400000L).toString())
                .build();
        refreshRepository.save(refreshRotate);

        //response
        response.setHeader("access", newAccess);
        response.addCookie(CookieUtils.createCookie("refresh", newRefresh));

        return ResponseEntity
                .ok()
                .body(ReissueResponse.builder().newAccessToken(newAccess).build());
    }
}
