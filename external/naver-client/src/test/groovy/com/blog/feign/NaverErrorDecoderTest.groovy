package com.blog.feign

import com.blog.NaverErrorResponse
import com.blog.exception.ApiException
import com.blog.exception.ErrorType
import com.fasterxml.jackson.databind.ObjectMapper
import feign.Request
import feign.Response
import org.springframework.http.HttpStatus
import spock.lang.Specification

class NaverErrorDecoderTest extends Specification {

    ObjectMapper objectMapper = Mock()
    NaverErrorDecoder naverErrorDecoder

    void setup(){
        naverErrorDecoder = new NaverErrorDecoder(objectMapper)
    }

    def "errorDecoder에서 error 발생시 ApiException이 throw 한다."(){
        given:
        def responseBody = Mock(Response.Body)
        def inputStream = new ByteArrayInputStream()
        def response = Response.builder()
                .status(400)
                .request(Request.create(Request.HttpMethod.GET, "testUrl", [:], null as Request.Body, null))
                .body(responseBody)
                .build()

        // responseBody.asInputStream()가 1번 호출되면  inputStream 반환
        1 * responseBody.asInputStream() >> inputStream
        // objectMapper.readValue(*_)가 1번 호출되면  NaverErrorResponse("error!!", "SEO3") 반환
        1 * objectMapper.readValue(*_) >> new NaverErrorResponse("error!!", "SEO3")

        when:
        naverErrorDecoder.decode(_ as String, response)

        then:
        ApiException exception = thrown()
        verifyAll(exception) {
            errorMessage == "error!!"
            errorType == ErrorType.EXTERNAL_API_ERROR
            httpStatus == HttpStatus.BAD_REQUEST
        }
    }

}

/*
(): 인자가 없는 메서드 호출
(*_): 어떤 인자든 허용 -> 호출 자체만 확인하고 싶을 때 사용
(_ as String, response): 타입 캐스팅을 통한 인자 매칭 -> 1st: 아무 String 값, 2nd: 정확히 response 객체
*/