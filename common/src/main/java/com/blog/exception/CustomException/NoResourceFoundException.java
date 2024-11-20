package com.blog.exception.CustomException;

import lombok.Getter;

@Getter
public class NoResourceFoundException extends RuntimeException{
    private final String errorMessage;

    public NoResourceFoundException(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
