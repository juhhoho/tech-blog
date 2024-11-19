package com.blog.post.dto.response;

import com.blog.post.entity.Reply;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PostBlogPostsResponse (
        Long id,
        String title,
        String description,
        LocalDateTime lastBuildTime,
        List<Reply> replies
){
}
