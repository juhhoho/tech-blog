package com.blog.post.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LikeFeedResponse {
    Long userId;
    Long feedId;
    int likeCount;
}
