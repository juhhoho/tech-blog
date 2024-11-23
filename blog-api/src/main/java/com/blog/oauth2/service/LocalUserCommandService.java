package com.blog.oauth2.service;

import com.blog.exception.CustomException.AuthInfoException;
import com.blog.oauth2.dto.request.RegisterLocalUserRequest;
import com.blog.oauth2.dto.response.RegisterLocalUserResponse;
import com.blog.oauth2.entity.LocalUser;
import com.blog.oauth2.repository.LocalUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocalUserCommandService {
    private final PasswordEncoder passwordEncoder;
    private final LocalUserRepository localUserRepository;

    public ResponseEntity<RegisterLocalUserResponse> registerLocalUser(RegisterLocalUserRequest registerLocalUserRequest) {
        log.info("[LocalUserCommandService - registerLocalUser] registerLocalUserRequest = {}", registerLocalUserRequest);


        if(localUserRepository.existsByIdentifier(registerLocalUserRequest.getIdentifier())){
            throw new AuthInfoException(registerLocalUserRequest.getIdentifier() + "는 이미 가입 정보가 존재하는 identifier입니다.");
        }
        if(localUserRepository.existsByEmail(registerLocalUserRequest.getEmail())){
            throw new AuthInfoException(registerLocalUserRequest.getEmail() + "는 이미 가입 정보가 존재하는 email입니다.");
        }

        // 관리자 계정 1개 -> id가 ADMIN
        if(registerLocalUserRequest.getIdentifier().equals("ADMIN")){
            LocalUser localAdminUser = LocalUser.builder()
                    .identifier(registerLocalUserRequest.getIdentifier())
                    .name(registerLocalUserRequest.getName())
                    .email(registerLocalUserRequest.getEmail())
                    .role("ROLE_ADMIN")
                    .password(passwordEncoder.encode(registerLocalUserRequest.getPassword()))
                    .build();

            LocalUser savedLocalUser = localUserRepository.saveAndFlush(localAdminUser);

            RegisterLocalUserResponse response = RegisterLocalUserResponse.builder()
                    .localUserId(savedLocalUser.getId())
                    .localUserIdentifier(savedLocalUser.getIdentifier())
                    .role(savedLocalUser.getRole())
                    .build();

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);
        }
        // 일반 계정
        else{
            LocalUser localUser = LocalUser.builder()
                    .identifier(registerLocalUserRequest.getIdentifier())
                    .name(registerLocalUserRequest.getName())
                    .email(registerLocalUserRequest.getEmail())
                    .role("ROLE_USER")
                    .password(passwordEncoder.encode(registerLocalUserRequest.getPassword()))
                    .build();

            LocalUser savedLocalUser = localUserRepository.saveAndFlush(localUser);

            RegisterLocalUserResponse response = RegisterLocalUserResponse.builder()
                    .localUserId(savedLocalUser.getId())
                    .localUserIdentifier(savedLocalUser.getIdentifier())
                    .role(savedLocalUser.getRole())
                    .build();

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);
        }



    }
}

