package com.blog.post.repository.feed;

import com.blog.post.entity.Feed;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedSpecRepository extends JpaRepository<Feed, Long>, JpaSpecificationExecutor<Feed> {

}
