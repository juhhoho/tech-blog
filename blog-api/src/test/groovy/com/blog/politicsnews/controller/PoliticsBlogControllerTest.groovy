package com.blog.politicsnews.controller

import com.blog.politicsnews.service.PoliticsNewsApplicationService
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

class PoliticsBlogControllerTest extends Specification {
    PoliticsBlogController politicsBlogController
    MockMvc mockMvc

    PoliticsNewsApplicationService politicsNewsApplicationService = Mock()

    void setup(){
        politicsBlogController = new PoliticsBlogController(politicsNewsApplicationService)
        mockMvc = MockMvcBuilders.standaloneSetup(politicsBlogController).build()
    }

    def "컨트롤러의 search 메서드가 정상적으로 동작한다."(){
        given:
        def givenQuery = "HTTP"
        def givenPage = 1
        def givenSize = 1

        when:
        def response = mockMvc
                .perform(MockMvcRequestBuilders.get("/v1/blog/news?query=${givenQuery}&page=${givenPage}&size=${givenSize}"))
                .andReturn()
                .response


        then:
        response.status == HttpStatus.OK.value()

        and:
        1 * politicsNewsApplicationService.searchAndSave(*_) >> {
            String query, int page, int size ->
                assert query == givenQuery
                assert page == givenPage
                assert size == givenSize
        }
    }
}
