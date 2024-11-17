package com.blog.post.controller

import com.blog.politicsnews.controller.PoliticsBlogController
import com.blog.politicsnews.service.PoliticsNewsApplicationService
import com.blog.post.service.PostQueryService
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

class PostControllerTest extends Specification{
    PostController postController
    MockMvc mockMvc

    PostQueryService postQueryService = Mock()

    void setup(){
        postController = new PostController(postQueryService)
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build()
    }

    def "컨트롤러의 search 메서드가 정상적으로 동작한다."(){
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
        1 * postQueryService.getBlogPosts(*_) >> {
            int page, int size ->
                assert page == givenPage
                assert size == givenSize
        }
    }
}
