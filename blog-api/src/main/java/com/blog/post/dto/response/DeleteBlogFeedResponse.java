package com.blog.post.dto.response;

import lombok.Builder;

@Builder
public record DeleteBlogFeedResponse(
        Long id
) {
}
