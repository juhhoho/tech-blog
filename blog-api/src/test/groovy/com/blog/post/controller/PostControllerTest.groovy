package com.blog.post.controller

import com.blog.post.dto.request.PostBlogPostsRequest
import com.blog.post.dto.response.PostBlogPostsResponse
import com.blog.post.service.PostApplicationService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import java.time.LocalDateTime

class PostControllerTest extends Specification{
    PostController postController

    PostApplicationService postApplicationService = Mock()
    MockMvc mockMvc



    void setup(){
        postController = new PostController(postApplicationService)
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build()
    }

    def "컨트롤러의 getBlogPosts 메서드가 정상적으로 동작한다."(){
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

    def "컨트롤러의 postBlogPosts 메서드가 정상적으로 동작한다."() {
        given:
        def givenTitle = "ex_title"
        def givenDescription = "ex_description"

        def requestPayload = new PostBlogPostsRequest(givenTitle, givenDescription)
        def expectedResponse = PostBlogPostsResponse.builder()
                .id(1L)
                .title(givenTitle)
                .description(givenDescription)
                .lastBuildTime(LocalDateTime.now())
                .build()

        // 서비스 계층의 Mock 동작 설정
        postApplicationService.postBlogPosts(givenTitle, givenDescription) >> ResponseEntity
                .status(HttpStatus.CREATED)
                .body(expectedResponse)

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/v1/blog/posts")
                .contentType(MediaType.APPLICATION_JSON)
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
        1 * postApplicationService.postBlogPosts(givenTitle, givenDescription) >>{
            String title, String description ->
                assert title == givenTitle
                assert description == givenDescription
        }
    }
}
