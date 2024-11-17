package com.blog.post.service

import spock.lang.Specification

class PostApplicationServiceTest extends Specification {

    PostApplicationService postApplicationService

    PostCommandService postCommandService = Mock()
    PostQueryService postQueryService = Mock()

    void setup(){
        postApplicationService = new PostApplicationService(postCommandService, postQueryService)
    }

    def "postBlogPosts 인자를 수정없이 넘겨준다."(){
        given:
        def givenTitle = "ex_title"
        def givenDescription = "ex_description"


        when:
        postApplicationService.postBlogPosts(givenTitle, givenDescription)

        then:
        1 * postCommandService.postBlogPosts(*_) >>{
            String title, String description ->
                assert title == givenTitle
                assert description == givenDescription
        }
    }

    def "getBlogPosts 인자를 수정없이 넘겨준다."(){
        given:
        def givenPage = 1
        def givenSize = 3


        when:
        postApplicationService.getBlogPosts(givenPage, givenSize)

        then:
        1 * postQueryService.getBlogPosts(*_) >>{
            int page, int size ->
                assert page == givenPage
                assert size == givenSize
        }
    }
}
