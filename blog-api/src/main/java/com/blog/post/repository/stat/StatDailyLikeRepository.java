package com.blog.post.repository.stat;

import com.blog.post.entity.StatDailyLike;
import com.blog.post.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface StatDailyLikeRepository extends JpaRepository<StatDailyLike, Long> {

    // 특정 feed의 일간 추천수 반환
    long countByFeedAndLikeDateTimeBetween(Feed feed, LocalDateTime start, LocalDateTime end);

}
