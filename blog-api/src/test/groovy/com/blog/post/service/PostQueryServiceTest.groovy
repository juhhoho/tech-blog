package com.blog.post.service

import com.blog.post.entity.Post
import com.blog.post.repository.PostRepository
import org.springframework.security.core.userdetails.User
import spock.lang.Specification

import java.time.LocalDateTime

class PostQueryServiceTest extends Specification {
    PostQueryService postQueryService
    PostRepository postRepository = Mock()

    void setup(){
        postQueryService = new PostQueryService(postRepository)
    }


    def "List<Post>를 PageResult<GetBlogPostsResponse>로 적절히 변환한다"() {
        given: "PostRepository에서 반환할 Mock 데이터 정의"
        def givenPage = 1
        def givenSize = 3
        def givenUser = com.blog.oauth2.entity.User.builder().name("ex_name").build()

        def mockPosts = [
                new Post("ex_title1","ex_description1", LocalDateTime.now(),givenUser),
                new Post("ex_title2","ex_description2", LocalDateTime.now().minusMinutes(10),givenUser),
                new Post("ex_title3","ex_description3", LocalDateTime.now().minusMinutes(20),givenUser)
        ]

        postRepository.findAllByOrderByLastBuildTimeDesc() >> mockPosts

        when: "PostQueryService의 getBlogPosts 메서드 호출"
        def result = postQueryService.getAllBlogPosts(givenPage, givenSize)

        then: "PageResult가 기대한 값과 일치해야 함"
        result.page() == givenPage
        result.size() == givenSize
        result.totalElements() == mockPosts.size()
        result.contents().size() == mockPosts.size()

        and: "각 Post가 올바르게 변환되었는지 확인"
        result.contents()[0].title == mockPosts[0].title
        result.contents()[0].description == mockPosts[0].description
        result.contents()[0].lastBuildTime == mockPosts[0].lastBuildTime

        result.contents()[1].title == mockPosts[1].title
        result.contents()[1].description == mockPosts[1].description
        result.contents()[1].lastBuildTime == mockPosts[1].lastBuildTime

        result.contents()[2].title == mockPosts[2].title
        result.contents()[2].description == mockPosts[2].description
        result.contents()[2].lastBuildTime == mockPosts[2].lastBuildTime

        print "${result.contents()[0]}"

    }
}

