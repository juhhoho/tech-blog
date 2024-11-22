package com.blog.politicsnews.service

import com.blog.politicsnews.repository.DailyStatRepository
import org.springframework.data.domain.Pageable
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime

class StatQueryServiceTest extends Specification {
    DailyStatQueryService dailyStatQueryService

    DailyStatRepository dailyStatRepository = Mock()

    void setup(){
        dailyStatQueryService = new DailyStatQueryService(dailyStatRepository)
    }

    def "findQueryCount 조회 시 하루치를 조회하면서 쿼리 개수가 반환된다."() {
        given:
        def givenQuery = "HTTP"
        def givenDate = LocalDate.of(2024, 5, 2)
        def expectedCount = 10

        when:
        def response = dailyStatQueryService.findQueryCount(givenQuery, givenDate)

        then:
        1 * dailyStatRepository.countByQueryAndEventDateTimeBetween(
                givenQuery,
                LocalDateTime.of(2024, 5, 2, 0, 0, 0),
                LocalDateTime.of(2024, 5, 2, 23, 59, 59, 999999999)
        ) >> expectedCount

        and:
        response.count() == 10

    }
    def "findTop5Query 조회 시 조회수 순으로 5개의 쿼리가 {query, count} 포맷으로 반환된다."(){
        given:
        when:
        dailyStatQueryService.findTop5Query()

        then:
        1 * dailyStatRepository.findTopQuery(*_)>>{
            Pageable pageable ->
                assert pageable.getPageNumber() == 0
                assert pageable.getPageSize() == 5
        }
    }
}
