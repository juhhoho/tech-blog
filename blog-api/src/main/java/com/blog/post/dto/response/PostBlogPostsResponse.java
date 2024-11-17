package com.blog.post.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PostBlogPostsResponse (
        Long id,
        String title,
        String description,
        LocalDateTime lastBuildTime
){
}
