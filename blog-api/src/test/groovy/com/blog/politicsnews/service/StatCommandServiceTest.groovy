package com.blog.politicsnews.service

import com.blog.politicsnews.entity.DailyStat
import com.blog.politicsnews.repository.dailystat.DailyStatRepository
import spock.lang.Specification

import java.time.LocalDateTime

class StatCommandServiceTest extends Specification {
    DailyStatCommandService dailyStatCommandService

    DailyStatRepository dailyStatRepository = Mock()

    void setup(){
        dailyStatCommandService = new DailyStatCommandService(dailyStatRepository)
    }


    def "save 호출 시 넘어온 인자 그대로 호출된다."(){
        given:
        def givenQuery = "ex_query"
        def givenDateTime = LocalDateTime.of(2024,5,3,0,0,0)
        def givenDailyStat = new DailyStat(givenQuery, givenDateTime)

        when:
        dailyStatCommandService.save(givenDailyStat)
        then:
        1* dailyStatRepository.save(*_) >> {
            DailyStat dailyStat ->
                assert dailyStat.query == givenQuery
                assert dailyStat.eventDateTime == givenDateTime
        }

    }
}
