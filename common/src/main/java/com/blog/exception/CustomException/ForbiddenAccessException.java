package com.blog.exception.CustomException;

import lombok.Getter;

@Getter
public class ForbiddenAccessException extends RuntimeException{
    private final String errorMessage;

    public ForbiddenAccessException(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
