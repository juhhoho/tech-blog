package com.blog.post.controller

import com.blog.oauth2.jwt.JWTUtil
import com.blog.post.dto.except.ReplyDtoExceptFeedAndUser
import com.blog.post.dto.except.UserDtoExceptFeeds
import com.blog.post.dto.request.MakeBlogFeedRequest
import com.blog.post.dto.response.GetOneBlogFeedResponse
import com.blog.post.dto.response.MakeBlogFeedResponse
import com.blog.post.dto.response.MakeReplyResponse
import com.blog.post.service.feed.FeedApplicationService
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import java.time.LocalDateTime

class FeedControllerTest extends Specification{
    FeedController feedController

    FeedApplicationService feedApplicationService = Mock()
    JWTUtil jwtUtil = Mock()
    MockMvc mockMvc

    void setup(){
        feedController = new FeedController(feedApplicationService, jwtUtil)
        mockMvc = MockMvcBuilders.standaloneSetup(feedController).build()
    }

    def "[GET] getAllBlogFeeds - page,size를 줄 때 정상적으로 동작한다."() {
        given:
        def givenPage = 1
        def givenSize = 1

        when: "page와 size를 줄 때"
        def response = mockMvc
                .perform(MockMvcRequestBuilders.get("/v1/blog/feeds?page=${givenPage}&size=${givenSize}"))
                .andReturn()
                .response

        then:
        response.status == HttpStatus.OK.value()

        and:
        1 * feedApplicationService.getAllBlogFeeds(*_) >> {
            int page, int size ->
                assert page == givenPage
                assert size == givenSize
        }
    }


    def "[GET] getAllBlogFeeds - page,size를 주지 않을 떄 때 디폴트 값으로 정상적으로 동작한다."(){
        given:
        def defaultPage = 1
        def defaultSize = 5

        when: "page와 size를 줄 때"
        def response = mockMvc
                .perform(MockMvcRequestBuilders.get("/v1/blog/feeds"))
                .andReturn()
                .response
        then:
        response.status == HttpStatus.OK.value()

        and:
        1 * feedApplicationService.getAllBlogFeeds(*_) >> {
            int page, int size ->
                assert page == defaultPage
                assert size == defaultSize
        }
    }


    def "[POST] makeBlogFeed - 정상적으로 동작한다."() {
        given:
        def givenTitle = "ex_title"
        def givenDescription = "ex_description"

        def request = MakeBlogFeedRequest.builder()
                .title(givenTitle)
                .description(givenDescription)
                .build()

        def givenUsername = "ex_username"
        def givenUserId = 1L
        def givenLikeCount = 1
        def fixedTime = LocalDateTime.of(2024, 11, 21, 10, 0)

        def expectedResponse = MakeBlogFeedResponse.builder()
                .id(1L)
                .title(givenTitle)
                .description(givenDescription)
                .lastBuildTime(fixedTime)
                .userId(givenUserId)
                .likeCount(givenLikeCount)
                .build()

        // 서비스 계층의 Mock 동작 설정
        feedApplicationService.makeBlogFeed(request.title, request.description, givenUsername) >> ResponseEntity
                .status(HttpStatus.CREATED)
                .body(expectedResponse)

        jwtUtil.getUsernameFromCookies(_ as HttpServletRequest) >> givenUsername

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/v1/blog/feeds")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "asdas")
                .content("""
                {
                    "title": "${givenTitle}",
                    "description": "${givenDescription}"
                }
            """))
                .andReturn()
                .response

        then:
        response.status == HttpStatus.OK.value()

        and:
        1 * feedApplicationService.makeBlogFeed(givenTitle, givenDescription, givenUsername)
    }

    def "[GET] 컨트롤러의 getOneBlogFeed - 정상적으로 동작한다."() {
        given:
        def givenPostId = 1L

        def expectedResponse = GetOneBlogFeedResponse.builder()
                .id(givenPostId)
                .title("ex_title")
                .description("ex_description")
                .lastBuildTime(LocalDateTime.now())
                .user(new UserDtoExceptFeeds(1l, "qwe","asd","zxc","qaz"))
                .replies(List.of(new ReplyDtoExceptFeedAndUser(1l, "asd")))
                .build()

        // 서비스 계층의 Mock 동작 설정
        feedApplicationService.getOneBlogFeed(givenPostId) >> ResponseEntity
                .ok()
                .body(expectedResponse)

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/v1/blog/feeds/${givenPostId}")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .response

        then:
        response.status == HttpStatus.OK.value()

        and:
        1 * feedApplicationService.getOneBlogFeed(givenPostId) >> {
            Long postId ->
                assert postId == givenPostId
        }
    }




}
