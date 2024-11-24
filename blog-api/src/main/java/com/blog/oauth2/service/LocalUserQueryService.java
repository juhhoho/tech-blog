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

        // 1000ms -> 1s
        String token = jwtUtil.createJwt(localUser.getIdentifier(), localUser.getRole(), 60*60*1000L);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

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
}
