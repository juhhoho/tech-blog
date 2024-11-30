package com.blog.post.service.feed;

import com.blog.pagination.PageResult;
import com.blog.post.dto.response.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class FeedApplicationService {
    private final FeedCommandService feedCommandService;
    private final FeedQueryService feedQueryService;

    public PageResult<GetBlogFeedsResponse> getAllBlogFeeds(int page, int size) {
        return feedQueryService.getAllBlogFeeds(page, size);
    }

    public ResponseEntity<MakeBlogFeedResponse> makeBlogFeed(String title, String description, String username) {
        return feedCommandService.makeBlogFeed(title, description, username);
    }

    public ResponseEntity<GetOneBlogFeedResponse> getOneBlogFeed(Long feedId, HttpServletRequest req, HttpServletResponse res) {
        // 누적 조회 수 업데이트
        feedCommandService.viewCountUp(feedId, req, res);

        // getOneBlogFeed
        return feedQueryService.getOneBlogFeed(feedId);
    }

    public PageResult<GetBlogFeedsResponse> getSomeBlogFeeds(int page, int size, String keyword) {
        return feedQueryService.getSomeBlogFeeds(page, size, keyword);
    }

    public ResponseEntity<UpdateBlogFeedResponse> updateBlogFeed(Long feedId, String title, String description, String username){
        return feedCommandService.updateBlogFeed(feedId, title, description, username);
    }

    public ResponseEntity<DeleteBlogFeedResponse> deleteBlogFeed(Long feedId , String username){
        return feedCommandService.deleteBlogFeed(feedId, username);
    }



}
