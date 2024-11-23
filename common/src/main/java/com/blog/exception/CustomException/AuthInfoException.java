package com.blog.exception.CustomException;

import lombok.Getter;

@Getter
public class AuthInfoException extends RuntimeException{
    private final String errorMessage;
    public AuthInfoException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
