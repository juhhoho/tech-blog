package com.blog.post.controller

import com.blog.oauth2.jwt.JWTUtil
import com.blog.post.dto.response.GetOneBlogFeedResponse
import com.blog.post.dto.response.MakeBlogFeedResponse
import com.blog.post.dto.response.MakeReplyResponse
import com.blog.post.service.feed.FeedApplicationService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import java.time.LocalDateTime

class FeedControllerTest extends Specification{
    FeedController postController

    FeedApplicationService postApplicationService = Mock()
    JWTUtil jwtUtil = Mock()
    MockMvc mockMvc



    void setup(){
        postController = new FeedController(postApplicationService, jwtUtil)
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build()
    }

    def "[GET] 컨트롤러의 getBlogPosts 메서드가 정상적으로 동작한다."(){
        given:
        def givenPage = 1
        def givenSize = 1

        when:
        def response = mockMvc
                .perform(MockMvcRequestBuilders.get("/v1/blog/posts?page=${givenPage}&size=${givenSize}"))
                .andReturn()
                .response


        then:
        response.status == HttpStatus.OK.value()

        and:
        1 * postApplicationService.getBlogPosts(*_) >> {
            int page, int size ->
                assert page == givenPage
                assert size == givenSize
        }
    }

    def "[POST] 컨트롤러의 postBlogPosts 메서드가 정상적으로 동작한다."() {
        given:
        def givenTitle = "ex_title"
        def givenDescription = "ex_description"
        def givenUsername = "ex_username"
        def givenAuthHeader = "ex_header"
        def givenName = "ex_name"
        def expectedResponse = MakeBlogFeedResponse.builder()
                .id(1L)
                .title(givenTitle)
                .description(givenDescription)
                .lastBuildTime(LocalDateTime.now())
                .name(givenName)
                .build()

        // 서비스 계층의 Mock 동작 설정
        postApplicationService.postBlogPosts(givenTitle, givenDescription,givenUsername) >> ResponseEntity
                .status(HttpStatus.CREATED)
                .body(expectedResponse)

        jwtUtil.getUsernameFromAuthorizationHeader(givenAuthHeader) >> givenUsername

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/v1/blog/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "${givenAuthHeader}")
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
        1 * postApplicationService.postBlogPosts(givenTitle, givenDescription, givenUsername) >>{
            String title, String description, String username ->
                assert title == givenTitle
                assert description == givenDescription
                assert username == givenUsername
        }
    }

    def "[GET] 컨트롤러의 getOneBlogPost 메서드가 정상적으로 동작한다."() {
        given:
        def givenPostId = 1L

        def expectedResponse = GetOneBlogFeedResponse.builder()
                .title("ex_title")
                .description("ex_description")
                .lastBuildTime(LocalDateTime.now())
                //.replies()
                .build()

        // 서비스 계층의 Mock 동작 설정
        postApplicationService.getOneBlogPosts(givenPostId) >> ResponseEntity
                .ok()
                .body(expectedResponse)

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/v1/blog/posts/${givenPostId}")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .response

        then:
        response.status == HttpStatus.OK.value()

        and:
        1 * postApplicationService.getOneBlogPosts(givenPostId) >> {
            Long postId ->
                assert postId == givenPostId
        }
    }

    def "[POST] 컨트롤러의 postReply 메서드가 정상적으로 동작한다."(){
        given:
        def givenPostId = 1l
        def givenReplyId = 1L
        def givenContent = "ex_content"

        def expectedResponse = MakeReplyResponse.builder()
                .postId(givenPostId)
                .replyId(givenReplyId)
                .content(givenContent)
                .build()

        // 서비스 계층의 Mock 동작 설정
        postApplicationService.postReply(givenPostId, givenContent) >> ResponseEntity
                                                                        .status(HttpStatus.CREATED)
                                                                        .body(expectedResponse)

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/v1/blog/posts/${givenPostId}/reply")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "content" : "${givenContent}"
                    }
                """))
                .andReturn()
                .response
        then:
        response.status == HttpStatus.CREATED.value()

    }



}
