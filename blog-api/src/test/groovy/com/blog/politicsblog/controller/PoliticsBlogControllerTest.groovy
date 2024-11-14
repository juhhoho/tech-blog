package com.blog.politicsblog.controller

import com.blog.politicsblog.service.PoliticsNewsApplicationService
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
                .perform(MockMvcRequestBuilders.get("/v1/blog/news?query=${givenQuery}&start=${givenPage}&display=${givenSize}"))
                .andReturn()
                .response


        then:
        response.status == HttpStatus.OK.value()

        and:
        1 * politicsNewsApplicationService.search(*_) >> {
            String query, int page, int size ->
                assert query == givenQuery
                assert page == givenPage
                assert size == givenSize
        }
    }
}
