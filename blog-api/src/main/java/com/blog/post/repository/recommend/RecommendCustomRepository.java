package com.blog.post.repository.recommend;

import com.blog.auth.entity.BaseUser;
import com.blog.post.entity.Feed;
import com.blog.post.entity.Recommend;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecommendCustomRepository {
    Optional<Recommend> checkDuplicate(BaseUser user, Feed feed, String type);
}
