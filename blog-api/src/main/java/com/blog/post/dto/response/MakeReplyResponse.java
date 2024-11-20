package com.blog.post.dto.response;

import lombok.Builder;


@Builder
public record MakeReplyResponse(
        Long feedId,
        Long replyId,
        Long userId,
        String content
){
}
