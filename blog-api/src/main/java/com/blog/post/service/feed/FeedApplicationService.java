package com.blog.post.service.feed;

import com.blog.pagination.PageResult;
import com.blog.post.dto.response.GetBlogFeedsResponse;
import com.blog.post.dto.response.GetOneBlogFeedResponse;
import com.blog.post.dto.response.MakeBlogFeedResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedApplicationService {
    private final FeedCommandService feedCommandService;
    private final FeedQueryService feedQueryService;

    public PageResult<GetBlogFeedsResponse> getAllBlogFeeds(int page, int size) {
        return feedQueryService.getAllBlogFeeds(page, size);
    }

    public PageResult<GetBlogFeedsResponse> getMostLikedBlogFeeds(int page, int size, int count){
        return feedQueryService.getMostLikedBlogFeeds(page, size, count);
    }

    public PageResult<GetBlogFeedsResponse> getMostViewedBlogFeeds(int page, int size, int count){
        return feedQueryService.getMostViewedBlogFeeds(page, size, count);
    }

    public ResponseEntity<MakeBlogFeedResponse> makeBlogFeed(String title, String description, String username) {
        return feedCommandService.makeBlogFeed(title, description, username);
    }

    public ResponseEntity<GetOneBlogFeedResponse> getOneBlogFeed(Long feedId, HttpServletRequest req, HttpServletResponse res) {
        // view count
        feedCommandService.viewCountUp(feedId, req, res);

        // getOneBlogFeed
        return feedQueryService.getOneBlogFeed(feedId);
    }

    public PageResult<GetBlogFeedsResponse> getSomeBlogFeeds(int page, int size, String title, String description) {
        return feedQueryService.getSomeBlogFeeds(page, size, title, description);
    }




}
