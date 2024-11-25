package com.blog.auth.dto.response;

import lombok.Builder;

@Builder
public record ReissueResponse(
        String newAccessToken
) {
}
