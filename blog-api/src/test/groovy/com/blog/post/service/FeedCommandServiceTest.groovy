package com.blog.post.service

import com.blog.post.entity.Feed
import com.blog.post.entity.Reply
import com.blog.post.repository.feed.FeedRepository
import com.blog.post.repository.reply.ReplyRepository
import com.blog.post.service.feed.FeedCommandService
import org.springframework.http.HttpStatus
import spock.lang.Specification

import java.time.LocalDateTime

class FeedCommandServiceTest extends Specification {
    FeedCommandService postCommandService

    FeedRepository postRepository = Mock()
    ReplyRepository replyRepository = Mock()

    void setup(){
        postCommandService = new FeedCommandService(postRepository, replyRepository)
    }

    def "postBlogPosts 메서드는 블로그 게시글을 저장하고 ResponseEntity<PostBlogPostsResponse>를 반환한다."() {
        given:
        // Given 데이터 생성
        def givenPost = Feed.builder()
                .title("ex_title1")
                .description("ex_description1")
                .lastBuildTime(LocalDateTime.of(2024, 1, 1, 1, 1, 1))
                .build()
        givenPost.setIdForTest(1l);

        // saveAndFlush Mock 동작 정의
        postRepository.saveAndFlush(*_) >> givenPost

        when:
        // 테스트 실행
        def result = postCommandService.postBlogPosts("ex_title1", "ex_description1")

        then:
        // 검증
        result.statusCode == HttpStatus.CREATED
        with(result.body) {
            id() == 1L
            title()  == "ex_title1"
            description() == "ex_description1"
            lastBuildTime() == LocalDateTime.of(2024, 1, 1, 1, 1, 1)
        }
    }

    def "postReply 메서드는 댓글을 작성하고 ResponseEntity<PostReplyResponse>를 반환"(){
        given:
        def givenPost = Feed.builder()
                .title("ex_title")
                .description("ex_desc")
                .lastBuildTime(LocalDateTime.now()).build()

        givenPost.setIdForTest(1L)

        def givenReply = Reply.builder()
                .content("ex_content")
                .post(givenPost).build()

        postRepository.findById(*_) >> Optional.of(givenPost)
        replyRepository.saveAndFlush(*_) >> givenReply

        when:
        def result = postCommandService.postReply(1l, "ex_content")

        then:
        verifyAll (result){
            statusCode == HttpStatus.CREATED
            with(body){
                postId() == 1L
                content() == "ex_content"
            }
        }
    }


}
