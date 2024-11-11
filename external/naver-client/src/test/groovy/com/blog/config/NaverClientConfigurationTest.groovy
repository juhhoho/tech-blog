package com.blog.config

import feign.RequestTemplate
import spock.lang.Specification

class NaverClientConfigurationTest extends Specification {
    NaverClientConfiguration naverClientConfiguration

    void setup(){
        naverClientConfiguration = new NaverClientConfiguration()
    }

    def "requestInterceptor를 거치면 header에 key값들이 적용된다."(){

        given:
        def givenRequestTemplate = new RequestTemplate()
        def givenClientId = "qwer"
        def givenClientSecret = "asdf"

        and: "interceptor를 타기 전에는 header에 key값이 존재하지 않는다."
        givenRequestTemplate.headers()["X-Naver-Client-Id"] == null
        givenRequestTemplate.headers()["X-Naver-Client-Secret"] == null

        when: "interceptor를 탄다."
        def interceptor = naverClientConfiguration.requestInterceptor(givenClientId, givenClientSecret)
        interceptor.apply(givenRequestTemplate)

        then: "header에 키 값이 적용된다."
        givenRequestTemplate.headers()["X-Naver-Client-Id"].contains(givenClientId)
        givenRequestTemplate.headers()["X-Naver-Client-Secret"].contains(givenClientSecret)
    }



}
