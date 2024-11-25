package com.blog.post.dto.response;

import lombok.Builder;

@Builder
public record DeleteReplyResponse(
        Long replyId
) {
}
