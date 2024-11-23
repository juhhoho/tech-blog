package com.blog.exception;

import com.blog.exception.CustomException.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /*
    모든 에러를 전부 예상하고 처리할 수 없기 때문에 일단은 서버 내부적인 에러로 처리
    */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e){
        log.error("Exception occurred. message = {}, className = {}", e.getMessage(), e.getClass().getName());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(ErrorType.UNKNOWN.getDescription(), ErrorType.UNKNOWN));
    }
    // ----------------------------------------------------------------------------------------------------------------
    // <external>
    // ----------------------------------------------------------------------------------------------------------------
    /*
    외부 api 통신에서 발생하는 에러
    */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException e){
        log.error("Api Exception occurred. message = {}, className = {}", e.getErrorMessage(), e.getClass().getName());
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ErrorResponse(e.getErrorMessage(), e.getErrorType()));
    }
    // ----------------------------------------------------------------------------------------------------------------
    // <blog-api>
    // ----------------------------------------------------------------------------------------------------------------
    /*
    db 조회와 관련 x
    validation 제약에 의한 인자의 적정성 판단
    */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException e){
        log.error("Bind Exception occurred. message = {}, className = {}", e.getMessage(), e.getClass().getName());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(createBindExceptionMessage(e), ErrorType.INVALID_PARAMETER));
    }

    /*
    인자의 적정성 판단 x
    해당 인자를 바탕으로 db를 조회하여 값이 존재하지 않을 때 사용
    */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException e){
        log.error("No Resource Exception occurred. message = {}, className = {}", e.getMessage(), e.getClass().getName());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getErrorMessage(), ErrorType.RESOURCE_NOT_FOUND));
    }

    /*
    사용자가 접근 권한이 없는 컨텐츠에 접근했을 때 사용
    서버가 사용자의 요청을 이해했지만 권한이 부족한 경우
    */
    @ExceptionHandler(ForbiddenAccessException.class)
    public ResponseEntity<ErrorResponse> handleNoAuthContentException(ForbiddenAccessException e){
        log.error("No Authentication Exception for Content occurred. message = {}, className = {}", e.getMessage(), e.getClass().getName());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(e.getErrorMessage(), ErrorType.FORBIDDEN_ACCESS));
    }


    @ExceptionHandler(RecommendException.class)
    public ResponseEntity<ErrorResponse> handleLikeDupException(RecommendException e){
        log.error("Recommend Exception occurred. message = {}, className = {}", e.getErrorMessage(), e.getClass().getName());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getErrorMessage(), ErrorType.INVALID_PARAMETER));
    }

    @ExceptionHandler(AuthInfoException.class)
    public ResponseEntity<ErrorResponse> handleAuthInfoException(AuthInfoException e){
        log.error("Authentication Information Exception occurred. message = {}, className = {}", e.getErrorMessage(), e.getClass().getName());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getErrorMessage(), ErrorType.INVALID_PARAMETER));
    }




    // ----------------------------------------------------------------------------------------------------------------
    private String createBindExceptionMessage(BindException e){
        if (e.getFieldError() != null && e.getFieldError().getDefaultMessage() != null ) {
            return e.getFieldError().getDefaultMessage();
        }
        return e.getFieldErrors()
                .stream()
                .map(FieldError::getField)
                .collect(Collectors.joining(", ")) + "  정확하지 않습니다.";
    }
}
