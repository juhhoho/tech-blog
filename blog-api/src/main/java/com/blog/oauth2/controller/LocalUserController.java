package com.blog.oauth2.controller;

import com.blog.oauth2.dto.request.LoginLocalUserRequest;
import com.blog.oauth2.dto.request.RegisterLocalUserRequest;
import com.blog.oauth2.dto.response.LoginLocalUserResponse;
import com.blog.oauth2.dto.response.RegisterLocalUserResponse;
import com.blog.oauth2.service.LocalUserApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LocalUserController {

    private final LocalUserApplicationService localUserApplicationService;

    @PostMapping("/luser/register")
    public ResponseEntity<RegisterLocalUserResponse> registerLocalUser(
            @Valid @RequestBody RegisterLocalUserRequest registerLocalUserRequest
    )
    {
        log.info("[LocalUserController - registerLocalUser] registerLocalUserRequest = {}", registerLocalUserRequest);

        return localUserApplicationService.registerLocalUser(registerLocalUserRequest);
    }

    @PostMapping("/luser/login")
    public ResponseEntity<LoginLocalUserResponse> loginLocalUser(
            @Valid @RequestBody LoginLocalUserRequest loginLocalUserRequest
    )
    {
        log.info("[LocalUserController - loginLocalUser] loginLocalUserRequest = {}", loginLocalUserRequest);

        return localUserApplicationService.loginLocalUser(loginLocalUserRequest);
    }

}