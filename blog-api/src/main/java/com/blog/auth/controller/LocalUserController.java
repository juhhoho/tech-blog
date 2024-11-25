package com.blog.auth.controller;

import com.blog.auth.dto.request.LoginLocalUserRequest;
import com.blog.auth.dto.request.RegisterLocalUserRequest;
import com.blog.auth.dto.response.LoginLocalUserResponse;
import com.blog.auth.dto.response.RegisterLocalUserResponse;
import com.blog.auth.service.user.LocalUserApplicationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LocalUserController {

    private final LocalUserApplicationService localUserApplicationService;

    @PostMapping("/register")
    public ResponseEntity<RegisterLocalUserResponse> registerLocalUser(
            @Valid @RequestBody RegisterLocalUserRequest registerLocalUserRequest
    )
    {
        log.info("[LocalUserController - registerLocalUser] registerLocalUserRequest = {}", registerLocalUserRequest);

        return localUserApplicationService.registerLocalUser(registerLocalUserRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginLocalUserResponse> loginLocalUser(
            @Valid @RequestBody LoginLocalUserRequest loginLocalUserRequest,
            HttpServletResponse response
    )
    {
        log.info("[LocalUserController - loginLocalUser] loginLocalUserRequest = {}", loginLocalUserRequest);

        return localUserApplicationService.loginLocalUser(loginLocalUserRequest, response);
    }
}