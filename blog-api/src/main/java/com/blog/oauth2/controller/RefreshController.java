package com.blog.oauth2.controller;

import com.blog.oauth2.dto.response.ReissueResponse;
import com.blog.oauth2.service.refresh.RefreshApplicationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class RefreshController {
    private final RefreshApplicationService refreshApplicationService;

    @PostMapping("/reissue")
    public ResponseEntity<ReissueResponse> reissue(HttpServletRequest request, HttpServletResponse response) {
        log.info("[ReissueController - reissue]");

        return refreshApplicationService.reissue(request, response);
    }
}
