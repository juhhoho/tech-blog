package com.blog.post.service

import com.blog.post.service.feed.FeedApplicationService
import com.blog.post.service.feed.FeedCommandService
import com.blog.post.service.feed.FeedQueryService
import spock.lang.Specification

class FeedApplicationServiceTest extends Specification {

    FeedApplicationService feedApplicationService

    FeedCommandService feedCommandService = Mock()
    FeedQueryService feedQueryService = Mock()

    void setup(){
        feedApplicationService = new FeedApplicationService(feedCommandService, feedQueryService)
    }

    def "postBlogPosts 인자를 수정없이 넘겨준다."(){
        given:
        def givenTitle = "ex_title"
        def givenDescription = "ex_description"
        def givenUsername = "qwer"


        when:
        feedApplicationService.makeBlogFeed(givenTitle, givenDescription, givenUsername)

        then:
        1 * feedCommandService.makeBlogFeed(*_) >>{
            String title, String description, String username ->
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

    def "postReply 인자를 수정없이 넘겨준다."(){
        given:
        def givenPostId = 1L
        def givenContent = "ex_content"

        when:
        postApplicationService.postReply(givenPostId, givenContent)

        then:
        1 * postCommandService.postReply(givenPostId, givenContent) >> {
            Long postId, String content ->
                assert postId == givenPostId
                assert content == givenContent
        }
    }

}
