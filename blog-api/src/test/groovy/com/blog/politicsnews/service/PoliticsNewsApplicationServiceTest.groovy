package com.blog.politicsnews.service


import spock.lang.Specification

import java.time.LocalDate

class PoliticsNewsApplicationServiceTest extends Specification {
    PoliticsNewsApplicationService politicsNewsApplicationService

    PoliticsNewsQueryService politicsNewsQueryService = Mock()
    DailyStatQueryService dailyStatQueryService = Mock()
    DailyStatCommandService dailyStatCommandService = Mock()

    void setup(){
        politicsNewsApplicationService = new PoliticsNewsApplicationService(politicsNewsQueryService, dailyStatCommandService, dailyStatQueryService)
    }

    def "searchAndSave 메서드 호출 시 인자를 변경없이 넘겨준다."(){
        given:
        def givenQuery = "ex_query"
        def givenPage = 1
        def givenSize = 1

        when:
        politicsNewsApplicationService.searchAndSave(givenQuery, givenPage, givenSize)

        then:
        1 * politicsNewsQueryService.search(*_) >>{
            String query, int page, int size ->
                assert query == givenQuery
                assert page == givenPage
                assert size == givenSize
        }
    }

    def "findQueryCount 메서드 호출 시 인자를 변경없이 넘겨준다."(){
        given:
        def givenQuery = "ex_query"
        def givenDate = LocalDate.of(2024, 5, 1)

        when:
        politicsNewsApplicationService.findQueryCount(givenQuery, givenDate)

        then:
        1 * dailyStatQueryService.findQueryCount(*_) >> {
            String query, LocalDate date ->
                assert query == givenQuery
                assert date == givenDate
        }
    }

    def "findTop5Query 메서드 호출시 DailyStatQueryService의 findTop5Query가 호출된다."(){

        given:
        when:
        politicsNewsApplicationService.findTop5Query()

        then:
        1 * dailyStatQueryService.findTop5Query()

    }

}
