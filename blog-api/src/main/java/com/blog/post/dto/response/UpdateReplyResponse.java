package com.blog.post.dto.response;

import lombok.Builder;

@Builder
public record UpdateReplyResponse(
        Long feedId,
        Long replyId,
        Long userId,
        String newContent
) {
}
