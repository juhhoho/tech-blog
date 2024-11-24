package com.blog.oauth2.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
public record ReissueResponse(
        String newAccessToken
) {
}
