package com.blog.post.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UnlikeFeedResponse {
    Long userId;
    Long feedId;
    int likeCount;
}
