package com.blog.post.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record UpdateBlogFeedResponse(
        Long id,
        String title,
        String description,
        LocalDateTime createTime,
        LocalDateTime lastBuildTime,
        Long userId
) {
}
