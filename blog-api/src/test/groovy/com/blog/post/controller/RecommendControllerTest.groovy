package com.blog.post.controller

import com.blog.auth.jwt.JWTUtil
import com.blog.post.dto.response.LikeFeedResponse
import com.blog.post.service.recommend.RecommendApplicationService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

class RecommendControllerTest extends Specification {

    RecommendController recommendController

    RecommendApplicationService recommendApplicationService = Mock()
    JWTUtil jwtUtil = Mock()
    MockMvc mockMvc

    void setup() {
        recommendController = new RecommendController(recommendApplicationService, jwtUtil)
        mockMvc = MockMvcBuilders.standaloneSetup(recommendController).build()
    }

    def "[POST] likeFeed - 정상적으로 동작"() {
        given:
        def username = "qwer"
        def expectedResponse = LikeFeedResponse.builder()
                .userId(1l)
                .feedId(1l)
                .likeCount(1).build()


        recommendApplicationService.likeFeed(*_) >>
                ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(expectedResponse)

        jwtUtil.getUsernameFromCookies(*_) >> username

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/v1/blog/feeds/${1}/like")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .response


        then:
        response.status == HttpStatus.CREATED.value()
    }

//    def "[DELETE] unlikeFeed - 정상적으로 동작"() {
//        given:
//        def username = "qwer"
//        def expectedResponse = UnlikeFeedResponse.builder()
//                .userId(1l)
//                .feedId(1l)
//                .likeCount(1).build()
//
//
//        recommendApplicationService.unlikeFeed(*_) >>
//                ResponseEntity
//                        .ok()
//                        .body(expectedResponse)
//
//        jwtUtil.getUsernameFromCookies(*_) >> username
//
//        when:
//        def response = mockMvc.perform(MockMvcRequestBuilders.delete("/v1/blog/feeds/${1}/like")
//                .header("Authorization", "qwe"))
//                .andReturn()
//                .response
//
//
//        then:
//        response.status == HttpStatus.OK.value()
//    }

}