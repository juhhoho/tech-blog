package com.blog.politicsnews.repository

import com.blog.feign.NaverClient
import com.blog.politicsnews.entity.DailyStat
import jakarta.persistence.EntityManager
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import java.time.LocalDateTime

@DataJpaTest
@ActiveProfiles("test")
class DailyStatDailyLikeRepositoryTest extends Specification {

    @Autowired
    DailyStatRepository dailyStatRepository
    @Autowired
    EntityManager entityManager

    @SpringBean
    NaverClient naverClient = Mock()

    def "저장 후 조회가 된다."() {
        given:
        def givenQuery = "ex_query"
        def givenDailyStat = new DailyStat(givenQuery, LocalDateTime.now())

        when:
        def saved = dailyStatRepository.saveAndFlush(givenDailyStat)

        then: "실제 db에 저장이 된다."
        saved.id != null

        when: "entity manager를 clear하고 재조회한다."
        entityManager.clear()

        def result = dailyStatRepository.findById(saved.id)

        then: "cache가 아닌 db에 쿼리를 날려 데이터를 조회한다."
        verifyAll {
            result.isPresent()
            result.get().query == givenQuery
        }
    }

    def "특정 query의 count를 조회한다."(){
        given:
        def givenQuery = "ex_query"
        def giveDateTime = LocalDateTime.of(2024, 5,2,0,0,0,)

        def givenStat1 = new DailyStat(givenQuery, giveDateTime.plusMinutes(10))
        def givenStat2 = new DailyStat(givenQuery, giveDateTime.minusMinutes(10))
        def givenStat3 = new DailyStat(givenQuery, giveDateTime.plusMinutes(10))
        def givenStat4 = new DailyStat("ex_another_query", giveDateTime.plusMinutes(10))

        dailyStatRepository.saveAll([givenStat1, givenStat2, givenStat3, givenStat4])

        when: "countByQueryAndEventDateTimeBetween를 통해 count를 얻는다"
        def count = dailyStatRepository.countByQueryAndEventDateTimeBetween(givenQuery, giveDateTime, giveDateTime.plusDays(1))

        then:
        count == 2
    }

    def "조회 수가 가장 높은 쿼리를 count와 함께 상위 3개 반환한다."(){
        given:
        def now = LocalDateTime.now()

        def stat1 = new DailyStat('JAVA', now.plusMinutes(10))
        def stat2 = new DailyStat('JAVA', now.plusMinutes(10))
        def stat3 = new DailyStat('JAVA', now.plusMinutes(10))
        def stat4 = new DailyStat('JAVA', now.plusMinutes(10))

        def stat5 = new DailyStat('HTTP', now.plusMinutes(10))
        def stat6 = new DailyStat('HTTP', now.plusMinutes(10))
        def stat7 = new DailyStat('HTTP', now.plusMinutes(10))

        def stat8 = new DailyStat('SPRING', now.plusMinutes(10))
        def stat9 = new DailyStat('SPRING', now.plusMinutes(10))

        def stat10 = new DailyStat('OS', now.plusMinutes(10))

        dailyStatRepository.saveAll([stat1, stat2, stat3, stat4, stat5, stat6, stat7, stat8, stat9, stat10])

        when:
        def request = PageRequest.of(0, 3)
        def response = dailyStatRepository.findTopQuery(request)

        then:
        verifyAll {
            response.size() == 3
            response[0].query() == 'JAVA'
            response[0].count() == 4
            response[1].query() == 'HTTP'
            response[1].count() == 3
            response[2].query() == 'SPRING'
            response[2].count() == 2

        }
    }

}
