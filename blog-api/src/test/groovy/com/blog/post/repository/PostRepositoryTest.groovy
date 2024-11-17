package com.blog.post.repository

import com.blog.feign.NaverClient
import com.blog.post.entity.Post
import jakarta.persistence.EntityManager
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import java.time.LocalDateTime

@DataJpaTest
@ActiveProfiles("test")
class PostRepositoryTest extends Specification {

    @Autowired
    PostRepository postRepository
    @Autowired
    EntityManager entityManager
    @SpringBean
    NaverClient naverClient = Mock()

    def "조회가 된다."(){
        given: "몇 개의 Post 엔티티를 저장한다."
        def post1 = new Post(title: "Title1", description: "Description1", lastBuildTime: LocalDateTime.of(2024, 11, 16, 10, 0))
        def post2 = new Post(title: "Title2", description: "Description2", lastBuildTime: LocalDateTime.of(2024, 11, 17, 12, 0))
        def post3 = new Post(title: "Title3", description: "Description3", lastBuildTime: LocalDateTime.of(2024, 11, 15, 9, 0))

        entityManager.persist(post1)
        entityManager.persist(post2)
        entityManager.persist(post3)
        entityManager.flush()
        entityManager.clear()

        when: "findAllByOrderByLastBuildTimeDesc 메서드를 호출한다."
        def result = postRepository.findAllByOrderByLastBuildTimeDesc()


        then: "결과가 lastBuildTime 내림차순으로 정렬되어, 즉 늦은 시간 순으로 반환된다."
        result.size() == 3
        result.get(0).title == "Title2"
        result.get(1).description == "Description1"
        result.get(2).lastBuildTime == LocalDateTime.of(2024, 11, 15, 9, 0)
    }

}
