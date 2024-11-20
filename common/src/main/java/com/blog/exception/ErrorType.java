package com.blog.exception;

import lombok.Getter;

@Getter
public enum ErrorType {
    EXTERNAL_API_ERROR("외부 API 호출 에러입니다."),
    UNKNOWN("알 수 없는 에러입니다."),
    INVALID_PARAMETER("잘못된 요청값입니다."),
    RESOURCE_NOT_FOUND("요청된 자원을 찾을 수 없습니다.");


    ErrorType(String description) {
        this.description = description;
    }

    private final String description;
}
