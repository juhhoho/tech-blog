package com.blog.auth.dto.response;

import lombok.Builder;

@Builder
public record RegisterLocalUserResponse(
        Long localUserId,
        String localUserIdentifier,
        String role
) {
}
