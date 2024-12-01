package com.blog.politicsnews.service;

import com.blog.pagination.PageResult;
import com.blog.politicsnews.dto.response.SearchResponse;
import com.blog.politicsnews.repository.polisticnews.PoliticsNewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PoliticsNewsQueryService {
    @Qualifier("naverPoliticsNewsRepositoryImpl")
    private final PoliticsNewsRepository naverPoliticsNewsRepository;

    public PageResult<SearchResponse> search(String query, int page, int size){
        log.info("[PoliticsNewsQueryService] naver query = {}, start= {}, display = {}", query, page, size);
        return naverPoliticsNewsRepository.search(query, page, size);
    }



}
