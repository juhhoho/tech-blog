package com.blog.post.repository.feed;

import com.blog.post.entity.Feed;

public interface FeedCommandRepository {
    void addLikeCount(Feed feed);

    void subLikeCount(Feed feed);
}
