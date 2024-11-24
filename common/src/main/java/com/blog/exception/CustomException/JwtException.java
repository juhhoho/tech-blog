package com.blog.exception.CustomException;

import lombok.Getter;

@Getter
public class JwtException extends RuntimeException{
    private final String errorMessage;
    public JwtException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
