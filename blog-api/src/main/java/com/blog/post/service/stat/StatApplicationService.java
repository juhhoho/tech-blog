package com.blog.post.service.stat;

import com.blog.pagination.PageResult;
import com.blog.post.dto.response.GetBlogFeedsResponse;
import com.blog.post.service.feed.FeedQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatApplicationService {

    private final StatQueryService statQueryService;

    public PageResult<GetBlogFeedsResponse> getMostLikedBlogFeeds(int page, int size, int count){
        return statQueryService.getMostLikedBlogFeeds(page, size, count);
    }

    public PageResult<GetBlogFeedsResponse> getMostDailyLikedBlogFeeds(int page, int size, int count, LocalDate today){
        return statQueryService.getMostDailyLikedBlogFeeds(page, size, count, today);
    }

    public PageResult<GetBlogFeedsResponse> getMostDislikedBlogFeeds(int page, int size, int count){
        return statQueryService.getMostDislikedBlogFeeds(page, size, count);
    }

    public PageResult<GetBlogFeedsResponse> getMostDailyDislikedBlogFeeds(int page, int size, int count, LocalDate today){
        return statQueryService.getMostDailyDislikedBlogFeeds(page, size, count, today);
    }

    public PageResult<GetBlogFeedsResponse> getMostViewedBlogFeeds(int page, int size, int count){
        return statQueryService.getMostViewedBlogFeeds(page, size, count);

    }
}
