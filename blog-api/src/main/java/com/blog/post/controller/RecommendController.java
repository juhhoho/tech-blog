package com.blog.post.controller;

import com.blog.auth.jwt.JWTUtil;
import com.blog.post.dto.response.DislikeFeedResponse;
import com.blog.post.dto.response.LikeFeedResponse;
import com.blog.post.dto.response.UnDislikeFeedResponse;
import com.blog.post.dto.response.UnlikeFeedResponse;
import com.blog.post.service.recommend.RecommendApplicationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/v1/blog")
@Slf4j
@RequiredArgsConstructor
public class RecommendController {
    private final RecommendApplicationService recommendApplicationService;
    private final JWTUtil jwtUtil;
    //------------------------------------------------------------------------------------------------------------------
    // <POST>
    //------------------------------------------------------------------------------------------------------------------

    // 게시글 추천
    @PostMapping("/feeds/{feed_id}/like")
    public ResponseEntity<LikeFeedResponse> likeFeed(
            @PathVariable("feed_id") Long feedId,
            HttpServletRequest request)
    {
        log.info("[RecommendController - likeFeed] feedId = {}, identifier = {}", feedId, jwtUtil.getIdentifierFromHttpRequest(request));
        return recommendApplicationService.likeFeed(feedId, jwtUtil.getIdentifierFromHttpRequest(request), LocalDateTime.now());
    }

    // 게시글 비추천
    @PostMapping("/feeds/{feed_id}/dislike")
    public ResponseEntity<DislikeFeedResponse> dislikeFeed(
            @PathVariable("feed_id") Long feedId,
            HttpServletRequest request)
    {
        log.info("[RecommendController - dislikeFeed] feedId = {}, identifier = {}", feedId, jwtUtil.getIdentifierFromHttpRequest(request));
        return recommendApplicationService.dislikeFeed(feedId, jwtUtil.getIdentifierFromHttpRequest(request), LocalDateTime.now());
    }

    //------------------------------------------------------------------------------------------------------------------
    // <DELETE>
    //------------------------------------------------------------------------------------------------------------------
    // 게시글 추천 취소
    @DeleteMapping("/feeds/{feed_id}/like")
    public ResponseEntity<UnlikeFeedResponse> unlikeFeed(
            @PathVariable("feed_id") Long feedId,
            HttpServletRequest request)
    {
        log.info("[RecommendController - unlikeFeed] feedId = {}, identifier = {}", feedId, jwtUtil.getIdentifierFromHttpRequest(request));
        return recommendApplicationService.unlikeFeed(feedId, jwtUtil.getIdentifierFromHttpRequest(request));
    }

    // 게시글 비추천 취소
    @DeleteMapping("/feeds/{feed_id}/dislike")
    public ResponseEntity<UnDislikeFeedResponse> unDislikeFeed(
            @PathVariable("feed_id") Long feedId,
            HttpServletRequest request)
    {
        log.info("[RecommendController - unDislikeFeed] feedId = {}, identifier = {}", feedId, jwtUtil.getIdentifierFromHttpRequest(request));
        return recommendApplicationService.unDislikeFeed(feedId, jwtUtil.getIdentifierFromHttpRequest(request));
    }
}
