package com.blog.politicsnews.controller;

import com.blog.politicsnews.dto.request.QueryStatsRequest;
import com.blog.politicsnews.dto.request.SearchRequest;
import com.blog.politicsnews.dto.response.PageResult;
import com.blog.politicsnews.dto.response.SearchResponse;
import com.blog.politicsnews.dto.response.StatResponse;
import com.blog.politicsnews.service.PoliticsNewsApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/blog")
@Slf4j
@RequiredArgsConstructor
public class PoliticsBlogController {
    private final PoliticsNewsApplicationService politicsNewsApplicationService;

    @GetMapping("/news")
    public PageResult<SearchResponse> search(@Valid SearchRequest searchRequest){
        log.info("[PoliticsBlogController - search] search = {}", searchRequest);
        return politicsNewsApplicationService.searchAndSave(searchRequest.getQuery(), searchRequest.getPage(), searchRequest.getSize());
    }

    @GetMapping("/news/stats")
    public StatResponse findQueryStats(@Valid QueryStatsRequest queryStatsRequest){
        log.info("[PoliticsBlogController] query stats = {}", queryStatsRequest );
        return politicsNewsApplicationService.findQueryCount(queryStatsRequest.getQuery(), queryStatsRequest.getDate());
    }

    @GetMapping("/news/stats/ranking")
    public List<StatResponse> findStatRanking(){
        log.info("[PoliticsBlogController] find top 5 stats");
        return politicsNewsApplicationService.findTop5Query();
    }

}
