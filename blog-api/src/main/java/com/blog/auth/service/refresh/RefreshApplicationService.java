package com.blog.auth.service.refresh;

import com.blog.auth.dto.response.ReissueResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshApplicationService {
    private final RefreshCommandService refreshCommandService;

    public ResponseEntity<ReissueResponse> reissue(HttpServletRequest request, HttpServletResponse response) {
        return refreshCommandService.reissue(request, response);
    }
}
