package com.blog.post.repository.feed;

import com.blog.post.entity.Feed;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FeedCustomRepository {

    List<Feed> getAllFeeds();

    void addLikeCount(Feed feed);

    void addDislikeCount(Feed feed);

    void subLikeCount(Feed feed);

    void subDislikeCount(Feed feed);

    void addViewCount(Feed feed);

    void updateFeed(Feed feed, String title, String description, LocalDateTime lasBuildTime);

    List<Feed> keywordSearch(String keyword);

    List<Feed> getNFeedsByMostLiked(int count);

    List<Feed> getNFeedsByMostDailyLiked(int count, LocalDate date);

    List<Feed> getNFeedsByMostDisliked(int count);

    List<Feed> getNFeedsByMostDailyDisliked(int count, LocalDate date);

    List<Feed> getNFeedsByMostViewed(int count);
}
