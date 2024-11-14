package com.blog.politicsblog.controller;

import com.blog.politicsblog.dto.request.SearchRequest;
import com.blog.politicsblog.dto.response.PageResult;
import com.blog.politicsblog.dto.response.SearchResponse;
import com.blog.politicsblog.service.PoliticsNewsApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/blog")
@Slf4j
@RequiredArgsConstructor
public class PoliticsBlogController {
    private final PoliticsNewsApplicationService politicsNewsApplicationService;

    @GetMapping("/news")
    public PageResult<SearchResponse> search(@Valid SearchRequest searchRequest){
        log.info("[PoliticsBlogController - search] search = {}", searchRequest);
        return politicsNewsApplicationService.search(searchRequest.getQuery(), searchRequest.getStart(), searchRequest.getDisplay());
    }
}
