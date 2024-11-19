package com.blog.post.specification;

import com.blog.post.entity.Post;
import org.springframework.data.jpa.domain.Specification;

public class PostSpecification {
    // title
    public static Specification<Post> searchTypeTitle(String searchKeyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + searchKeyword + "%");
    }
    // description
    public static Specification<Post> searchDescription(String searchKeyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("description"), "%" + searchKeyword + "%");
    }
}
