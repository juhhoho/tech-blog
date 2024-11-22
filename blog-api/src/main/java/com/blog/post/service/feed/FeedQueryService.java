package com.blog.post.service.feed;
import com.blog.exception.CustomException.NoResourceFoundException;
import com.blog.pagination.PageResult;
import com.blog.pagination.PaginateUtils;
import com.blog.post.dto.response.GetBlogFeedsResponse;
import com.blog.post.dto.response.GetOneBlogFeedResponse;
import com.blog.post.entity.Feed;
import com.blog.post.repository.feed.FeedRepository;
import com.blog.post.repository.feed.FeedSpecRepository;
import com.blog.post.specification.FeedSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedQueryService {

    private final FeedRepository feedRepository;
    private final FeedSpecRepository feedSpecRepository;

    // 모든 feeds 페이징 결과 반환
    public PageResult<GetBlogFeedsResponse> getAllBlogFeeds(int page, int size) {
        log.info("[FeedQueryService - getAllBlogFeeds] page = {}, size = {}", page, size);

        List<Feed> blogFeeds = feedRepository.findAllByOrderByCreateTimeDescLastBuildTimeDesc();

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

        // create time -> build time 순으로 정렬 조건 생성
        Sort sort = Sort.by(Sort.Order.desc("createTime"), Sort.Order.desc("lastBuildTime"));

        // 검색 + 정려 조건에 따라 검색된 모든 결과 가져오기
        List<Feed> blogFeeds = feedSpecRepository.findAll(spec, sort);

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
