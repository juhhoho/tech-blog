package com.blog.post.service.stat;

import com.blog.pagination.PageResult;
import com.blog.pagination.PaginateUtils;
import com.blog.post.dto.response.GetBlogFeedsResponse;
import com.blog.post.entity.Feed;
import com.blog.post.repository.stat.StatCustomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatQueryService {

    private final StatCustomRepository statCustomRepository;

    // 누적 추천수 기반 feeds 페이징 결과 반환
    public PageResult<GetBlogFeedsResponse> getMostLikedBlogFeeds(int page, int size, int count){
        log.info("[StatQueryService - getMostLikedBlogFeeds] page = {}, size = {}, count = {}", page, size, count);

        List<Feed> blogFeeds = statCustomRepository.getNFeedsByMostLiked(count);

        return PaginateUtils.paginate(blogFeeds, page, size, GetBlogFeedsResponse::convertToGetBlogFeedsResponse);
    }

    // 일간 추천수 기반 feeds 페이징 결과 반환
    public PageResult<GetBlogFeedsResponse> getMostDailyLikedBlogFeeds(int page, int size, int count, LocalDate date){
        log.info("[StatQueryService - getMostDailyLikedBlogFeeds] page = {}, size = {}, count = {}, today = {}", page, size, count, date);

        List<Feed> blogFeeds = statCustomRepository.getNFeedsByMostDailyLiked(count, date);

        return PaginateUtils.paginate(blogFeeds, page, size, GetBlogFeedsResponse::convertToGetBlogFeedsResponse);
    }

    // 누적 비추천수 기반 feeds 페이징 결과 반환
    public PageResult<GetBlogFeedsResponse> getMostDislikedBlogFeeds(int page, int size, int count){
        log.info("[StatQueryService - getMostDislikedBlogFeeds] page = {}, size = {}, count = {}", page, size, count);

        List<Feed> blogFeeds = statCustomRepository.getNFeedsByMostDisliked(count);

        return PaginateUtils.paginate(blogFeeds, page, size, GetBlogFeedsResponse::convertToGetBlogFeedsResponse);
    }

    // 일간 비추천수 기반 feeds 페이징 결과 반환
    public PageResult<GetBlogFeedsResponse> getMostDailyDislikedBlogFeeds(int page, int size, int count, LocalDate date){
        log.info("[StatQueryService - getMostDailyDislikedBlogFeeds] page = {}, size = {}, count = {}, today = {}", page, size, count, date);

        List<Feed> blogFeeds = statCustomRepository.getNFeedsByMostDailyDisliked(count, date);

        return PaginateUtils.paginate(blogFeeds, page, size, GetBlogFeedsResponse::convertToGetBlogFeedsResponse);
    }

    // 누적 조회수 기반 feeds 페이징 결과 반환
    public PageResult<GetBlogFeedsResponse> getMostViewedBlogFeeds(int page, int size, int count){
        log.info("[StatQueryService - getMostViewedBlogFeeds] page = {}, size = {}, count = {}", page, size, count);

        List<Feed> blogFeeds = statCustomRepository.getNFeedsByMostViewed(count);

        return PaginateUtils.paginate(blogFeeds, page, size, GetBlogFeedsResponse::convertToGetBlogFeedsResponse);
    }

}


