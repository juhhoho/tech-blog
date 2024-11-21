package com.blog.post.service.feed;
import com.blog.exception.CustomException.NoResourceFoundException;
import com.blog.pagination.PageResult;
import com.blog.pagination.PaginateUtils;
import com.blog.post.dto.response.GetBlogFeedsResponse;
import com.blog.post.dto.response.GetOneBlogFeedResponse;
import com.blog.post.entity.Feed;
import com.blog.post.repository.feed.FeedCommandRepository;
import com.blog.post.repository.feed.FeedRepository;
import com.blog.post.repository.feed.FeedSpecRepository;
import com.blog.post.specification.FeedSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedQueryService {

    private final FeedRepository feedRepository;
    private final FeedCommandRepository feedCommandRepository;
    private final FeedSpecRepository feedSpecRepository;

    // 모든 feeds 페이징 결과 반환
    public PageResult<GetBlogFeedsResponse> getAllBlogFeeds(int page, int size) {
        log.info("[FeedQueryService - getAllBlogFeeds] page = {}, size = {}", page, size);

        List<Feed> blogFeeds = feedRepository.findAllByOrderByLastBuildTimeDesc();

        return PaginateUtils.paginate(blogFeeds, page, size, GetBlogFeedsResponse::convertToGetBlogFeedsResponse);
    }

    // 추천수 기반 feeds 페이징 결과 반환
    public PageResult<GetBlogFeedsResponse> getMostLikedBlogFeeds(int page, int size, int count){
        log.info("[FeedQueryService - getMostLikedBlogFeeds] page = {}, size = {}, count = {}", page, size, count);

        List<Feed> blogFeeds = feedCommandRepository.getAllNFeedsByMostLiked(count);

        return PaginateUtils.paginate(blogFeeds, page, size, GetBlogFeedsResponse::convertToGetBlogFeedsResponse);
    }

    // 조회수 기반 feeds 페이징 결과 반환
    public PageResult<GetBlogFeedsResponse> getMostViewedBlogFeeds(int page, int size, int count){
        log.info("[FeedQueryService - getMostViewedBlogFeeds] page = {}, size = {}, count = {}", page, size, count);

        List<Feed> blogFeeds = feedCommandRepository.getAllNFeedsByMostViewed(count);

        return PaginateUtils.paginate(blogFeeds, page, size, GetBlogFeedsResponse::convertToGetBlogFeedsResponse);
    }

    // 검색어 기반 feeds 페이징 결과 반환
    public PageResult<GetBlogFeedsResponse> getSomeBlogFeeds(int page, int size, String title, String description) {
        log.info("[FeedQueryService - getSomeBlogFeeds] page = {}, size = {}, title = {}, description = {}", page, size, title, description);

        // Specification을 사용하여 검색 조건 생성
        Specification<Feed> spec = Specification.where(null);

        if (title != null && !title.isBlank()) {
            spec = spec.and(FeedSpecification.searchTypeTitle(title));
        }
        if (description != null && !description.isBlank()) {
            spec = spec.and(FeedSpecification.searchDescription(description));
        }

        // 조건에 따라 검색된 모든 결과 가져오기
        List<Feed> blogFeeds = feedSpecRepository.findAll(spec);

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
