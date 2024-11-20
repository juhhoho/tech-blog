package com.blog.post.specification;

import com.blog.post.entity.Feed;
import org.springframework.data.jpa.domain.Specification;

public class FeedSpecification {
    // title
    public static Specification<Feed> searchTypeTitle(String searchKeyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + searchKeyword + "%");
    }
    // description
    public static Specification<Feed> searchDescription(String searchKeyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("description"), "%" + searchKeyword + "%");
    }
}
