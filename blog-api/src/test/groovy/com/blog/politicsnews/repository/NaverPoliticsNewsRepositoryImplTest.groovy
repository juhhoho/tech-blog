package com.blog.politicsnews.repository

import com.blog.Item
import com.blog.NaverNewsResponse
import com.blog.feign.NaverClient
import com.blog.politicsnews.repository.polisticnews.NaverPoliticsNewsRepositoryImpl
import com.blog.politicsnews.repository.polisticnews.PoliticsNewsRepository
import spock.lang.Specification

import java.time.LocalDate

class NaverPoliticsNewsRepositoryImplTest extends Specification {
    NaverClient naverClient = Mock()
    PoliticsNewsRepository politicsBlogRepository

    void setup(){
        politicsBlogRepository = new NaverPoliticsNewsRepositoryImpl(naverClient)
    }

    def "search 호출 시 적절한 데이터 반환"(){
        given:
        def givenItems = [
                new Item("ex_title1", "ex_link1", "ex_desc1", "Fri, 01 Nov 2024 14:21:07 +0900"),
                new Item("ex_title2", "ex_link2", "ex_desc2", "Fri, 01 Nov 2024 14:21:07 +0900")
        ]
        def naverNewsResponse = new NaverNewsResponse(
                lastBuildDate: "Fri, 01 Nov 2024 14:21:07 +0900",
                total:2,
                start:1,
                display:2,
                items: givenItems
        )

        and:
        1 * naverClient.search("ex_query", 1, 2) >> naverNewsResponse

        when:
        def result = politicsBlogRepository.search("ex_query", 1, 2)

        then:
        verifyAll (result){
            page() == 1
            size() == 2
            totalElements() == 2
            contents().size() == 2
            contents().get(0).pubDate() == LocalDate.of(2024, 11,1)
            contents().get(1).pubDate() == LocalDate.of(2024, 11,1)
        }
    }
}
