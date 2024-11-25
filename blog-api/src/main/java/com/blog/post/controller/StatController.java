package com.blog.post.controller;

import com.blog.pagination.PageResult;
import com.blog.post.dto.response.GetBlogFeedsResponse;
import com.blog.post.service.stat.StatApplicationService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/v1/blog")
@Slf4j
@RequiredArgsConstructor
public class StatController {

    private final StatApplicationService statApplicationService;

    //------------------------------------------------------------------------------------------------------------------
    // <GET>
    //------------------------------------------------------------------------------------------------------------------

    // 누적 추천 수 기반 상위 n개 feeds stat
    // 페이징(page = 1, size = 5)
    @GetMapping("/stat/feeds/like")
    public PageResult<GetBlogFeedsResponse> getMostLikedBlogFeeds(
            @RequestParam(defaultValue = "1") @Min(1) @Max(10000) int page,
            @RequestParam(defaultValue = "5") @Min(1) @Max(50) int size,
            @RequestParam(defaultValue = "5") @Min(1) @Max(10) int count)
    {
        log.info("[FeedController - getMostLikedBlogFeeds] page = {}, size = {}, count = {}", page, size, count);
        return statApplicationService.getMostLikedBlogFeeds(page, size, count);
    }

    // 일간 추천 수 기반 상위 n개 feeds stat
    // 페이징(page = 1, size = 5)
    @GetMapping("/stat/feeds/like/daily")
    public PageResult<GetBlogFeedsResponse> getMostDailyLikedBlogFeeds(
            @RequestParam(defaultValue = "1") @Min(1) @Max(10000) int page,
            @RequestParam(defaultValue = "5") @Min(1) @Max(50) int size,
            @RequestParam(defaultValue = "5") @Min(1) @Max(10) int count,
            @RequestParam LocalDate date)
    {
        log.info("[FeedController - getMostDailyLikedBlogFeeds] page = {}, size = {}, count = {}, today = {}", page, size, count, date);
        return statApplicationService.getMostDailyLikedBlogFeeds(page, size, count, date);
    }


    // 누적 비추천 수 기반 상위 n개 feeds stat
    // 페이징(page = 1, size = 5)
    @GetMapping("/stat/feeds/dislike")
    public PageResult<GetBlogFeedsResponse> getMostDislikedBlogFeeds(
            @RequestParam(defaultValue = "1") @Min(1) @Max(10000) int page,
            @RequestParam(defaultValue = "5") @Min(1) @Max(50) int size,
            @RequestParam(defaultValue = "5") @Min(1) @Max(10) int count)
    {
        log.info("[FeedController - getMostDislikedBlogFeeds] page = {}, size = {}, count = {}", page, size, count);
        return statApplicationService.getMostDislikedBlogFeeds(page, size, count);
    }

    // 일간 비추천 수 기반 상위 n개 feeds stat
    // 페이징(page = 1, size = 5)
    @GetMapping("/stat/feeds/dislike/daily")
    public PageResult<GetBlogFeedsResponse> getMostDailyDislikedBlogFeeds(
            @RequestParam(defaultValue = "1") @Min(1) @Max(10000) int page,
            @RequestParam(defaultValue = "5") @Min(1) @Max(50) int size,
            @RequestParam(defaultValue = "5") @Min(1) @Max(10) int count,
            @RequestParam LocalDate date)
    {
        log.info("[FeedController - getMostDailyDislikedBlogFeeds] page = {}, size = {}, count = {}, today = {}", page, size, count, date);
        return statApplicationService.getMostDailyDislikedBlogFeeds(page, size, count, date);
    }


    // 누적 조회 수 기반 상위 n개 feeds stat
    // 페이징(page = 1, size = 5)
    // 동일 사용자의 24시간 이내 중복 조회는 무시 처리
    @GetMapping("/stat/feeds/view")
    public PageResult<GetBlogFeedsResponse> getMostViewedBlogFeeds(
            @RequestParam(defaultValue = "1") @Min(1) @Max(10000) int page,
            @RequestParam(defaultValue = "5") @Min(1) @Max(50) int size,
            @RequestParam(defaultValue = "5") @Min(1) @Max(10) int count)
    {
        log.info("[FeedController - getMostViewedBlogFeeds] page = {}, size = {}, count = {}", page, size, count);
        return statApplicationService.getMostViewedBlogFeeds(page, size, count);
    }
}
