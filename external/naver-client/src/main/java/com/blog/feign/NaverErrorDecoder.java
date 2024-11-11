package com.blog.feign;

import com.blog.NaverErrorResponse;
import com.blog.exception.ApiException;
import com.blog.exception.ErrorType;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Slf4j
public class NaverErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            String body = new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
            NaverErrorResponse errorResponse = objectMapper.readValue(body, NaverErrorResponse.class);
            throw new ApiException(errorResponse.errorMessage(), ErrorType.EXTERNAL_API_ERROR, HttpStatus.valueOf(response.status()));
        } catch (IOException e) {
            log.error("[NaverErrorDecoder - decode] 에러 메세지 파싱 오류 code = {}, request = {}, methodKey = {}, errorMessage = {}"
                    ,response.status(),  response.request(), methodKey, e.getMessage());
            throw new ApiException("네이버 에러 메세지 파싱 오류", ErrorType.EXTERNAL_API_ERROR, HttpStatus.valueOf(response.status()));
        }
    }
}
