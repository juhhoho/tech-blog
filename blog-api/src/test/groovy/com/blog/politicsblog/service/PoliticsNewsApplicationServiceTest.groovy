package com.blog.politicsblog.service

import spock.lang.Specification

class PoliticsNewsApplicationServiceTest extends Specification {
    PoliticsNewsApplicationService politicsNewsApplicationService

    PoliticsNewsQueryService politicsNewsQueryService = Mock()

    void setup(){
        politicsNewsApplicationService = new PoliticsNewsApplicationService(politicsNewsQueryService)
    }

    def "search 메서드 호출 시 인자를 변경없이 넘겨준다."(){
        given:
        def givenQuery = "ex_query"
        def givenPage = 1
        def givenSize = 1

        when:
        politicsNewsApplicationService.search(givenQuery, givenPage, givenSize)

        then:
        1 * politicsNewsQueryService.search(*_) >>{
            String query, int page, int size ->
                assert query == givenQuery
                assert page == givenPage
                assert size == givenSize
        }
    }
}
