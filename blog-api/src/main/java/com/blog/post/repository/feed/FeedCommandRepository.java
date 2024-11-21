package com.blog.post.repository.feed;

import com.blog.post.entity.Feed;

import java.util.List;

public interface FeedCommandRepository {
    void addLikeCount(Feed feed);

    void subLikeCount(Feed feed);

    List<Feed> getAllNFeedsByMostLiked(int count);

    List<Feed> getAllNFeedsByMostViewed(int count);
}
