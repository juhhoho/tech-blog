package com.blog.auth.dto.response;

import lombok.Builder;

@Builder
public record LoginLocalUserResponse(
        Long localUserId,
        String localUserIdentifier,
        String role
) {
}
