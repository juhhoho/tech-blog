package com.blog.politicsblog.service;

import com.blog.politicsblog.dto.response.PageResult;
import com.blog.politicsblog.dto.response.SearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class PoliticsNewsApplicationService {

    private final PoliticsNewsQueryService politicsNewsQueryService;

    public PageResult<SearchResponse> search(String query, int page, int size){
        log.info("[PoliticsNewsQueryService] naver query = {}, page= {}, size = {}", query, page, size);
        return politicsNewsQueryService.search(query, page, size);
    }
}
