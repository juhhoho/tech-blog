package com.blog.post.repository.feed;

import com.blog.post.entity.Feed;

import java.time.LocalDateTime;


public interface FeedCustomRepository {
    void addLikeCount(Feed feed);

    void subLikeCount(Feed feed);

    void updateFeedTitleDescriptionLastBuildTime(Feed feed, String title, String description, LocalDateTime lasBuildTime);
}
