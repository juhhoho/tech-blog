package com.blog.post.dto.response;

import com.blog.post.entity.Reply;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record MakeBlogFeedResponse(
        Long id,
        String title,
        String description,
        LocalDateTime lastBuildTime,
        Long userId,
        int likeCount,
        List<Reply> replies
){
}
