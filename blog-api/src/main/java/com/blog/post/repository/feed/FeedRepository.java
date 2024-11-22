package com.blog.post.repository.feed;

import com.blog.post.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
    List<Feed> findAllByOrderByCreateTimeDescLastBuildTimeDesc();
}
