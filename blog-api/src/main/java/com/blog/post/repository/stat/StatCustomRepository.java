package com.blog.post.repository.stat;

import com.blog.post.entity.Feed;

import java.time.LocalDate;
import java.util.List;

public interface StatCustomRepository {

    List<Feed> getNFeedsByMostLiked(int count);

    // 특정 date애 대해 그날의 추천수에 따른 n개의 feed를 반환
    List<Feed> getNFeedsByMostDailyLiked(int count, LocalDate date);

    List<Feed> getNFeedsByMostViewed(int count);

}
