package com.blog.post.dto.response;

import lombok.Builder;


@Builder
public record PostReplyResponse (
        Long postId,
        Long replyId,
        Long userId,
        String content
){
}
