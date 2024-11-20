package com.blog.post.controller;

import com.blog.oauth2.jwt.JWTUtil;
import com.blog.post.dto.response.LikeFeedResponse;
import com.blog.post.dto.response.UnlikeFeedResponse;
import com.blog.post.service.recommend.RecommendApplicationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/blog")
@Slf4j
@RequiredArgsConstructor
public class RecommendController {
    private final RecommendApplicationService recommendApplicationService;
    private final JWTUtil jwtUtil;

    // 게시글 추천
    @PostMapping("/feeds/{feed_id}/like")
    public ResponseEntity<LikeFeedResponse> likeFeed(
            @PathVariable("feed_id") Long feedId,
            HttpServletRequest request)
    {
        log.info("[RecommendController - likeFeed] feedId = {}, username = {}", feedId, jwtUtil.getUsernameFromCookies(request));
        return recommendApplicationService.likeFeed(feedId, jwtUtil.getUsernameFromCookies(request));
    }

    // 게시글 추천 취소
    @DeleteMapping("/feeds/{feed_id}/unlike")
    public ResponseEntity<UnlikeFeedResponse> unlikeFeed(
            @PathVariable("feed_id") Long feedId,
            HttpServletRequest request)
    {
        log.info("[RecommendController - unlikeFeed] feedId = {}, username = {}", feedId, jwtUtil.getUsernameFromCookies(request));
        return recommendApplicationService.unlikeFeed(feedId, jwtUtil.getUsernameFromCookies(request));
    }
}
