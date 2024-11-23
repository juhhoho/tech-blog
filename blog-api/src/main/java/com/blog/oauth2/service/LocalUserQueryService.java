package com.blog.oauth2.service;

import com.blog.exception.CustomException.AuthInfoException;
import com.blog.exception.CustomException.NoResourceFoundException;
import com.blog.oauth2.dto.request.LoginLocalUserRequest;
import com.blog.oauth2.dto.response.LoginLocalUserResponse;
import com.blog.oauth2.entity.LocalUser;
import com.blog.oauth2.jwt.JWTUtil;
import com.blog.oauth2.repository.LocalUserRepository;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocalUserQueryService {

    private final LocalUserRepository localUserRepository;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<LoginLocalUserResponse> loginLocalUser(LoginLocalUserRequest loginLocalUserRequest) {
        log.info("[LocalUserQueryService - loginLocalUser] loginLocalUserRequest = {}", loginLocalUserRequest);

        LocalUser localUser = localUserRepository.findLocalUserByIdentifier(loginLocalUserRequest.getIdentifier()).orElseThrow(
                ()-> new NoResourceFoundException(loginLocalUserRequest.getIdentifier() + "를 identifier로 갖는 localUser를 찾을 수 없습니다."));


        if (!passwordEncoder.matches(loginLocalUserRequest.getPassword(), localUser.getPassword())){
            throw new AuthInfoException("입력하신 password가 일치하지 않습니다.");
        }

        // JWT 생성
        // 토큰 지속시간: 10초
        // String token = jwtUtil.createJwt(username, role, 100000L);
        // 토큰 for local test
        String token = jwtUtil.createJwt(localUser.getIdentifier(), localUser.getRole(), 10000000L);

        // JWT를 쿠키에 추가
        Cookie cookieAuthorization = createCookie("Authorization", token);

        // 쿠키를 응답 헤더에 추가
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", String.format("%s=%s; Path=%s; Max-Age=%d; HttpOnly",
                cookieAuthorization.getName(),
                cookieAuthorization.getValue(),
                cookieAuthorization.getPath(),
                cookieAuthorization.getMaxAge()));

        LoginLocalUserResponse loginLocalUserResponse = LoginLocalUserResponse.builder()
                .localUserId(localUser.getId())
                .localUserIdentifier(localUser.getIdentifier())
                .role(localUser.getRole())
                .build();

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(loginLocalUserResponse);

    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        //cookie.setSecure(true); //https에서만 쿠키가 전달되게 함.
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
