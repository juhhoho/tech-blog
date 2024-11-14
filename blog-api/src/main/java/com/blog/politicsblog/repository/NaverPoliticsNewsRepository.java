package com.blog.politicsblog.repository;

import com.blog.Item;
import com.blog.NaverNewsResponse;
import com.blog.feign.NaverClient;
import com.blog.politicsblog.dto.response.PageResult;
import com.blog.politicsblog.dto.response.SearchResponse;
import com.blog.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class NaverPoliticsNewsRepository implements PoliticsNewsRepository {

    private final NaverClient naverClient;


    @Override
    public PageResult<SearchResponse> search(String query, int page, int size) {
        NaverNewsResponse naverNewsResponse = naverClient.search(query, page, size);

        List<SearchResponse> searchResponses = naverNewsResponse.getItems().stream()
                .map(this::createSearchResponse)
                .toList();
        return new PageResult<>(page, size, naverNewsResponse.getTotal(), searchResponses);

    }

    private SearchResponse createSearchResponse(Item item){
        
        return SearchResponse.builder()
                .title(item.title())
                .link(item.link())
                .description(item.description())
                .pubDate(DateUtils.parseDateWithTimeZone(item.pubDate()))
                .build();
    }
}
