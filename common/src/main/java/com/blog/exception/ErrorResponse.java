package com.blog.exception;

public record ErrorResponse (String errorMessage, ErrorType errorType){}