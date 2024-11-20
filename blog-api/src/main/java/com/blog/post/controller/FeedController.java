package com.blog.post.controller;

import com.blog.oauth2.jwt.JWTUtil;
import com.blog.pagination.PageResult;
import com.blog.post.dto.request.MakeBlogFeedRequest;
import com.blog.post.dto.response.GetBlogFeedsResponse;
import com.blog.post.dto.response.GetOneBlogFeedResponse;
import com.blog.post.dto.response.MakeBlogFeedResponse;
import com.blog.post.service.feed.FeedApplicationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/blog")
@Slf4j
@RequiredArgsConstructor
public class FeedController {
    private final FeedApplicationService feedApplicationService;
    private final JWTUtil jwtUtil;

    // 전체 post 조회 + 페이징
    @GetMapping("/feeds")
    public PageResult<GetBlogFeedsResponse> getAllBlogFeeds(
            @RequestParam(defaultValue = "1") @Min(1) @Max(10000) int page,
            @RequestParam(defaultValue = "5") @Min(1) @Max(50) int size)
    {
        log.info("[FeedController - getAllBlogFeeds] page = {}, size = {}", page, size);
        return feedApplicationService.getAllBlogFeeds(page, size);
    }

    // 키워드 기반 posts 조회 + 페이징
    @GetMapping("/feeds/search")
    public PageResult<GetBlogFeedsResponse> getSomeBlogFeeds(
            @RequestParam(defaultValue = "1") @Min(1) @Max(10000) int page,
            @RequestParam(defaultValue = "5") @Min(1) @Max(50) int size,
            @RequestParam(defaultValue = "") String title,
            @RequestParam(defaultValue = "") String description)
    {
        log.info("[FeedController - getSomeBlogFeeds] page = {}, size = {}, title = {}, description = {}", page, size, title, description);
        return feedApplicationService.getSomeBlogFeeds(page, size, title, description);
    }

    // post_id 기반 특정 post 조회
    @GetMapping("/feeds/{feed_id}")
    public ResponseEntity<GetOneBlogFeedResponse> getOneBlogFeed(
            @PathVariable("feed_id") Long feedId)
    {
        log.info("[FeedController - getOneBlogFeed] feed_id = {}", feedId);
        return feedApplicationService.getOneBlogFeed(feedId);
    }


    // post 작성
    @PostMapping("/feeds")
    public ResponseEntity<MakeBlogFeedResponse> makeBlogFeed(
            @Valid @RequestBody MakeBlogFeedRequest makeBlogFeedRequest,
            HttpServletRequest request)
    {
        log.info("[FeedController - makeBlogFeed] makeBlogFeedRequest = {}, username = {}", makeBlogFeedRequest, jwtUtil.getUsernameFromCookies(request));
        return feedApplicationService.makeBlogFeed(makeBlogFeedRequest.getTitle(), makeBlogFeedRequest.getDescription(), jwtUtil.getUsernameFromCookies(request));
    }



}

