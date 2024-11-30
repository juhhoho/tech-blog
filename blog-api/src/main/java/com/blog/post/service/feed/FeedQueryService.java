package com.blog.post.service.feed;
import com.blog.exception.CustomException.NoResourceFoundException;
import com.blog.pagination.PageResult;
import com.blog.pagination.PaginateUtils;
import com.blog.post.dto.response.GetBlogFeedsResponse;
import com.blog.post.dto.response.GetOneBlogFeedResponse;
import com.blog.post.entity.Feed;
import com.blog.post.repository.feed.FeedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedQueryService {

    private final FeedRepository feedRepository;

    // 모든 feeds 페이징 결과 반환
    public PageResult<GetBlogFeedsResponse> getAllBlogFeeds(int page, int size) {
        log.info("[FeedQueryService - getAllBlogFeeds] page = {}, size = {}", page, size);

        List<Feed> blogFeeds = feedRepository.getAllFeeds();

        return PaginateUtils.paginate(blogFeeds, page, size, GetBlogFeedsResponse::convertToGetBlogFeedsResponse);
    }

    // 검색어 기반 feeds 페이징 결과 반환
    public PageResult<GetBlogFeedsResponse> getSomeBlogFeeds(int page, int size, String keyword) {
        log.info("[FeedQueryService - getSomeBlogFeeds] page = {}, size = {}, keyword = {}", page, size, keyword);

        // keyword search, 만약 title, desc 모두 null 이면 전체 entity 조회
        List<Feed> blogFeeds = feedRepository.keywordSearch(keyword);

        return PaginateUtils.paginate(blogFeeds, page, size, GetBlogFeedsResponse::convertToGetBlogFeedsResponse);

    }

    // post_id로 특정 feed 반환
    public ResponseEntity<GetOneBlogFeedResponse> getOneBlogFeed(Long feedId) {
        log.info("[FeedQueryService - getOneBlogFeed] feed_id {}", feedId);

        Feed feed = feedRepository.findById(feedId).orElseThrow(
                ()-> new NoResourceFoundException(feedId + "를 id로 갖는 feed를 찾을 수 없습니다."));

        GetOneBlogFeedResponse oneBlogPostResponse = GetOneBlogFeedResponse.convertToGetOneBlogFeedResponse(feed);

        return ResponseEntity.ok().body(oneBlogPostResponse);
    }


}
