package com.blog.post.service

import com.blog.post.entity.Post
import com.blog.post.repository.PostRepository
import org.springframework.http.HttpStatus
import spock.lang.Specification

import java.time.LocalDateTime

class PostCommandServiceTest extends Specification {
    PostCommandService postCommandService

    PostRepository postRepository = Mock()

    void setup(){
        postCommandService = new PostCommandService(postRepository)
    }

    def "postBlogPosts 메서드는 블로그 게시글을 저장하고 ResponseEntity<PostBlogPostsResponse>를 반환한다."() {
        given:
        // Given 데이터 생성
        def givenPost = Post.builder()
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


}
