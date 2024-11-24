package com.blog.oauth2.service.user;

import com.blog.oauth2.dto.request.LoginLocalUserRequest;
import com.blog.oauth2.dto.request.RegisterLocalUserRequest;
import com.blog.oauth2.dto.response.LoginLocalUserResponse;
import com.blog.oauth2.dto.response.RegisterLocalUserResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocalUserApplicationService {
    private final LocalUserCommandService localUserCommandService;
    private final LocalUserQueryService localUserQueryService;

    public ResponseEntity<RegisterLocalUserResponse> registerLocalUser(RegisterLocalUserRequest registerLocalUserRequest) {
        return localUserCommandService.registerLocalUser(registerLocalUserRequest);
    }

    public ResponseEntity<LoginLocalUserResponse> loginLocalUser(LoginLocalUserRequest loginLocalUserRequest, HttpServletResponse response) {
        return localUserQueryService.loginLocalUser(loginLocalUserRequest, response);
    }
}
