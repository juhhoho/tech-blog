package com.blog.post.repository.recommend;

import com.blog.auth.entity.BaseUser;
import com.blog.post.entity.Feed;
import com.blog.post.entity.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecommendRepository extends JpaRepository<Recommend,Long> {
    Optional<Recommend> findByUserAndFeedAndType(BaseUser user, Feed feed, String type);
}
