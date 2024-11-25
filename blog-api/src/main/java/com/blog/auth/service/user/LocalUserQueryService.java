package com.blog.auth.service.user;

import com.blog.exception.CustomException.AuthInfoException;
import com.blog.exception.CustomException.NoResourceFoundException;
import com.blog.auth.dto.request.LoginLocalUserRequest;
import com.blog.auth.dto.response.LoginLocalUserResponse;
import com.blog.auth.entity.LocalUser;
import com.blog.auth.entity.Refresh;
import com.blog.auth.jwt.JWTUtil;
import com.blog.auth.repository.user.LocalUserRepository;
import com.blog.auth.repository.refresh.RefreshRepository;
import com.blog.util.CookieUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocalUserQueryService {

    private final LocalUserRepository localUserRepository;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RefreshRepository refreshRepository;

    public ResponseEntity<LoginLocalUserResponse> loginLocalUser(LoginLocalUserRequest loginLocalUserRequest, HttpServletResponse response) {
        log.info("[LocalUserQueryService - loginLocalUser] loginLocalUserRequest = {}", loginLocalUserRequest);

        LocalUser localUser = localUserRepository.findLocalUserByIdentifier(loginLocalUserRequest.getIdentifier()).orElseThrow(
                ()-> new NoResourceFoundException(loginLocalUserRequest.getIdentifier() + "를 identifier로 갖는 localUser를 찾을 수 없습니다."));


        if (!passwordEncoder.matches(loginLocalUserRequest.getPassword(), localUser.getPassword())){
            throw new AuthInfoException("입력하신 password가 일치하지 않습니다.");
        }

        // 토큰 생성
        String access = jwtUtil.createJwt("access", localUser.getIdentifier(), localUser.getRole(), 600000L);
        String refresh = jwtUtil.createJwt("refresh", localUser.getIdentifier(), localUser.getRole(), 86400000L);



        // access: 헤더, refresh: 토큰
        response.setHeader("access", access);
        response.addCookie(CookieUtils.createCookie("refresh", refresh));

        // save refresh for rotate
        Refresh refreshRotate = Refresh.builder()
                .username(localUser.getIdentifier())
                .refresh(refresh)
                .expiration(new Date(System.currentTimeMillis() + 86400000L).toString())
                .build();

        refreshRepository.save(refreshRotate);

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
