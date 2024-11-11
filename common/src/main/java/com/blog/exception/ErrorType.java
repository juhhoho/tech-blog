package com.blog.exception;

public enum ErrorType {
    EXTERNAL_API_ERROR("외부 API 호출 에러입니다.");



    ErrorType(String description) {
        this.description = description;
    }
    private final String description;
}
