package com.blog.post.controller;

import com.blog.auth.jwt.JWTUtil;
import com.blog.pagination.PageResult;
import com.blog.post.dto.request.MakeBlogFeedRequest;
import com.blog.post.dto.request.UpdateBlogFeedRequest;
import com.blog.post.dto.response.*;
import com.blog.post.service.feed.FeedApplicationService;
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
    //------------------------------------------------------------------------------------------------------------------

    // 전체 feeds 조회
    // 페이징(page = 1,size = 5)
    // 생성 시간 정렬, 만약 생성 시간이 같으면 수정 시간 정렬
    @GetMapping("/feeds")
    public PageResult<GetBlogFeedsResponse> getAllBlogFeeds(
            @RequestParam(defaultValue = "1") @Min(1) @Max(10000) int page,
            @RequestParam(defaultValue = "5") @Min(1) @Max(50) int size)
    {
        log.info("[FeedController - getAllBlogFeeds] page = {}, size = {}", page, size);
        return feedApplicationService.getAllBlogFeeds(page, size);
    }

    // 키워드 기반 feeds 검색
    // 페이징(page = 1, size = 5)
    // 생성 시간 정렬, 만약 생성 시간이 같으면 수정 시간 정렬
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


    // feed의 id값 기반 특정 feed 조회
    // 조회 수 카운트
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
    //------------------------------------------------------------------------------------------------------------------

    // feed 작성
    @PostMapping("/feeds")
    public ResponseEntity<MakeBlogFeedResponse> makeBlogFeed(
            @Valid @RequestBody MakeBlogFeedRequest makeBlogFeedRequest,
            HttpServletRequest request)
    {
        log.info("[FeedController - makeBlogFeed] makeBlogFeedRequest = {}, identifier = {}", makeBlogFeedRequest, jwtUtil.getIdentifierFromHttpRequest(request));
        return feedApplicationService.makeBlogFeed(makeBlogFeedRequest.getTitle(), makeBlogFeedRequest.getDescription(), jwtUtil.getIdentifierFromHttpRequest(request));
    }

    //------------------------------------------------------------------------------------------------------------------
    // <PATCH>
    //------------------------------------------------------------------------------------------------------------------

    // feed 업데이트
    @PatchMapping("/feeds/{feed_id}")
    public ResponseEntity<UpdateBlogFeedResponse> updateBlogFeed(
            @PathVariable("feed_id") Long feedId,
            @Valid @RequestBody UpdateBlogFeedRequest updateBlogFeedRequest,
            HttpServletRequest request)
    {
        log.info("[FeedController - updateBlogFeed] feedId = {} updateBlogFeedRequest = {}, identifier = {}", feedId, updateBlogFeedRequest, jwtUtil.getIdentifierFromHttpRequest(request));
        return feedApplicationService.updateBlogFeed(feedId, updateBlogFeedRequest.getTitle(), updateBlogFeedRequest.getDescription(), jwtUtil.getIdentifierFromHttpRequest(request));
    }

    //------------------------------------------------------------------------------------------------------------------
    // <DELETE>
    //------------------------------------------------------------------------------------------------------------------

    // feed 삭제
    @DeleteMapping("/feeds/{feed_id}")
    public ResponseEntity<DeleteBlogFeedResponse> deleteBlogFeed(
            @PathVariable("feed_id") Long feedId,
            HttpServletRequest request)
    {
        log.info("[FeedController - deleteBlogFeed] feedId = {}, identifier = {}", feedId, jwtUtil.getIdentifierFromHttpRequest(request));
        return feedApplicationService.deleteBlogFeed(feedId, jwtUtil.getIdentifierFromHttpRequest(request));
    }


}

