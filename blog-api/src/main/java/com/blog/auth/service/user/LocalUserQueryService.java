package com.blog.auth.service.user;

import com.blog.exception.CustomException.AuthInfoException;
import com.blog.exception.CustomException.NoResourceFoundException;
import com.blog.auth.dto.request.LoginLocalUserRequest;
import com.blog.auth.dto.response.LoginLocalUserResponse;
import com.blog.auth.entity.LocalUser;
import com.blog.auth.jwt.JWTUtil;
import com.blog.auth.repository.LocalUserRepository;
import com.blog.util.CookieUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocalUserQueryService {

    private final LocalUserRepository localUserRepository;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final StringRedisTemplate stringRedisTemplate;

    public ResponseEntity<LoginLocalUserResponse> loginLocalUser(LoginLocalUserRequest loginLocalUserRequest, HttpServletResponse response) {
        log.info("[LocalUserQueryService - loginLocalUser] loginLocalUserRequest = {}", loginLocalUserRequest);

        LocalUser localUser = localUserRepository.findLocalUserByIdentifier(loginLocalUserRequest.getIdentifier()).orElseThrow(
                ()-> new NoResourceFoundException(loginLocalUserRequest.getIdentifier() + "를 identifier로 갖는 localUser를 찾을 수 없습니다."));


        if (!passwordEncoder.matches(loginLocalUserRequest.getPassword(), localUser.getPassword())){
            throw new AuthInfoException("입력하신 password가 일치하지 않습니다.");
        }

        // 토큰 생성
        String newAccess = jwtUtil.createJwt("access", localUser.getIdentifier(), localUser.getRole(), 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", localUser.getIdentifier(), localUser.getRole(), 86400000L);


        // access: 헤더, refresh: 토큰
        response.setHeader("access", newAccess);
        response.addCookie(CookieUtils.createCookie("refresh", newRefresh));


        // redis -> newRefresh 저장(24시간), 즉 24시간 동안 로그인 유지
        stringRedisTemplate.opsForValue().set("refresh:identifier:" + loginLocalUserRequest.getIdentifier(),  newRefresh, 24 , TimeUnit.HOURS);

        // 응답 생성
        LoginLocalUserResponse loginLocalUserResponse = LoginLocalUserResponse.builder()
                .localUserId(localUser.getId())
                .localUserIdentifier(localUser.getIdentifier())
                .role(localUser.getRole())
                .build();

        return ResponseEntity
                .ok()
                .body(loginLocalUserResponse);
    }
}
