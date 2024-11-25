package com.blog.auth.service.user;

import com.blog.auth.dto.request.LoginLocalUserRequest;
import com.blog.auth.dto.request.RegisterLocalUserRequest;
import com.blog.auth.dto.response.LoginLocalUserResponse;
import com.blog.auth.dto.response.RegisterLocalUserResponse;
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
