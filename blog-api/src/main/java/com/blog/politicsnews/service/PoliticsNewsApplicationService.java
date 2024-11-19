package com.blog.politicsnews.service;

import com.blog.pagination.PageResult;
import com.blog.politicsnews.dto.response.SearchResponse;
import com.blog.politicsnews.dto.response.StatResponse;
import com.blog.politicsnews.entity.DailyStat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class PoliticsNewsApplicationService {

    private final PoliticsNewsQueryService politicsNewsQueryService;
    private final DailyStatCommandService dailyStatCommandService;
    private final DailyStatQueryService dailyStatQueryService;

    public PageResult<SearchResponse> searchAndSave(String query, int page, int size){
        log.info("[PoliticsNewsQueryService - searchAndSave] naver query = {}, page= {}, size = {}", query, page, size);
        PageResult<SearchResponse> searchResponse = politicsNewsQueryService.search(query, page, size);
        DailyStat dailyStat = new DailyStat(query, LocalDateTime.now());
        log.info("[PoliticsNewsQueryService - searchAndSave] dailyStat = {}", dailyStat);
        dailyStatCommandService.save(dailyStat);

        return searchResponse;

    }

    public StatResponse findQueryCount(String query, LocalDate date){
        return dailyStatQueryService.findQueryCount(query, date);
    }

    public List<StatResponse> findTop5Query(){
        return dailyStatQueryService.findTop5Query();
    }
}
