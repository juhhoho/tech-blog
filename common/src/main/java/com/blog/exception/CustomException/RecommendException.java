package com.blog.exception.CustomException;

import lombok.Getter;

@Getter
public class RecommendException extends RuntimeException{
    private final String errorMessage;

    public RecommendException(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
