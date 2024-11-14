package com.blog.politicsblog.service

import com.blog.politicsblog.repository.PoliticsNewsRepository
import spock.lang.Specification

class PoliticsNewsQueryServiceTest extends Specification {

    PoliticsNewsRepository naverPoliticsNewsRepository = Mock(PoliticsNewsRepository)
    PoliticsNewsQueryService politicsNewsQueryService

    void setup(){
        politicsNewsQueryService = new PoliticsNewsQueryService(naverPoliticsNewsRepository)
    }

    def "컨트롤러에서 넘어온 값이 변경없이 넘어간다."(){
        given:
        def givenQuery = "ex_query"
        def givenPage = 1
        def givenSize = 2
        when:
        politicsNewsQueryService.search(givenQuery,givenPage, givenSize)

        then: "컨트롤러에서 넘어온 값 검증"
        1 * naverPoliticsNewsRepository.search(*_) >> {
            String query, int page, int size ->
                assert query == givenQuery
                assert page == givenPage
                assert size == givenSize
        }


    }
}
