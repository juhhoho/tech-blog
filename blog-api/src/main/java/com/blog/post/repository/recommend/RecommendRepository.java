package com.blog.post.repository.recommend;

import com.blog.oauth2.entity.BaseUser;
import com.blog.post.entity.Feed;
import com.blog.post.entity.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecommendRepository extends JpaRepository<Recommend,Long> {
    Optional<Recommend> findByUserAndFeed(BaseUser user, Feed feed);
}
