package com.blog;

public record NaverErrorResponse(
        String errorMessage,
        String errorCode
) {
}
