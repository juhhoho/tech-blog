package com.blog.post.repository.feed;

import com.blog.post.entity.Feed;


public interface FeedCustomRepository {
    void addLikeCount(Feed feed);

    void subLikeCount(Feed feed);
}
