package com.blog.post.controller

import com.blog.post.dto.response.MakeReplyResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Specification

class ReplyControllerTest extends Specification {
    def "[POST] makeReply  - 정상적으로 동작한다."(){
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
