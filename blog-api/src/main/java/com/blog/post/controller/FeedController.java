package com.blog.post.controller;

import com.blog.oauth2.jwt.JWTUtil;
import com.blog.pagination.PageResult;
import com.blog.post.dto.request.MakeBlogFeedRequest;
import com.blog.post.dto.response.GetBlogFeedsResponse;
import com.blog.post.dto.response.GetOneBlogFeedResponse;
import com.blog.post.dto.response.MakeBlogFeedResponse;
import com.blog.post.service.feed.FeedApplicationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    //------------------------------------------------------------------------------------------------------------------
    // <GET>

    // 전체 feeds 조회 + 페이징(page = 1,size = 5) + 최근 게시일 순
    @GetMapping("/feeds")
    public PageResult<GetBlogFeedsResponse> getAllBlogFeeds(
            @RequestParam(defaultValue = "1") @Min(1) @Max(10000) int page,
            @RequestParam(defaultValue = "5") @Min(1) @Max(50) int size)
    {
        log.info("[FeedController - getAllBlogFeeds] page = {}, size = {}", page, size);
        return feedApplicationService.getAllBlogFeeds(page, size);
    }

    // 추천 수 기반 feeds 조회 + 페이징(page = 1, size = 5)
    @GetMapping("/feeds/like/ranking")
    public PageResult<GetBlogFeedsResponse> getMostLikedBlogFeeds(
            @RequestParam(defaultValue = "1") @Min(1) @Max(10000) int page,
            @RequestParam(defaultValue = "5") @Min(1) @Max(50) int size,
            @RequestParam(defaultValue = "5") @Min(1) @Max(10) int count)
    {
        log.info("[FeedController - getMostLikedBlogFeeds] page = {}, size = {}, count = {}", page, size, count);
        return feedApplicationService.getMostLikedBlogFeeds(page, size, count);
    }

    // 조회 수 기반 feeds 조회 + 페이징(page = 1, size = 5)
    @GetMapping("/feeds/view/ranking")
    public PageResult<GetBlogFeedsResponse> getMostViewedBlogFeeds(
            @RequestParam(defaultValue = "1") @Min(1) @Max(10000) int page,
            @RequestParam(defaultValue = "5") @Min(1) @Max(50) int size,
            @RequestParam(defaultValue = "5") @Min(1) @Max(10) int count)
    {
        log.info("[FeedController - getMostViewedBlogFeeds] page = {}, size = {}, count = {}", page, size, count);
        return feedApplicationService.getMostViewedBlogFeeds(page, size, count);
    }

    // 키워드 기반 feeds 조회 + 페이징(page = 1, size = 5) + 최근 게시일 순
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


    // feed_id 기반 특정 feed 조회 + 조회 수 카운트
    @GetMapping("/feeds/{feed_id}")
    public ResponseEntity<GetOneBlogFeedResponse> getOneBlogFeed(
            @PathVariable("feed_id") Long feedId,
            HttpServletRequest req,
            HttpServletResponse res)
    {
        log.info("[FeedController - getOneBlogFeed] feedId = {}", feedId);
        return feedApplicationService.getOneBlogFeed(feedId, req, res);
    }


    //------------------------------------------------------------------------------------------------------------------
    // <POST>

    // feed 작성
    @PostMapping("/feeds")
    public ResponseEntity<MakeBlogFeedResponse> makeBlogFeed(
            @Valid @RequestBody MakeBlogFeedRequest makeBlogFeedRequest,
            HttpServletRequest request)
    {
        log.info("[FeedController - makeBlogFeed] makeBlogFeedRequest = {}, username = {}", makeBlogFeedRequest, jwtUtil.getUsernameFromCookies(request));
        return feedApplicationService.makeBlogFeed(makeBlogFeedRequest.getTitle(), makeBlogFeedRequest.getDescription(), jwtUtil.getUsernameFromCookies(request));
    }



}

