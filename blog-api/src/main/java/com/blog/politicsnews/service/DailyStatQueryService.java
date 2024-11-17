package com.blog.politicsnews.service;

import com.blog.politicsnews.dto.response.StatResponse;
import com.blog.politicsnews.repository.DailyStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyStatQueryService {

    private final DailyStatRepository dailyStatRepository;
    public static final int PAGE = 0;
    public static final int SIZE = 5;

    public StatResponse findQueryCount(String query, LocalDate date){
        long count = dailyStatRepository.countByQueryAndEventDateTimeBetween(
                query,
                date.atStartOfDay(),
                date.atTime(LocalTime.MAX)
        );
        return new StatResponse(query, count);
    }

    public List<StatResponse> findTop5Query(){
        Pageable pageable = PageRequest.of(PAGE, SIZE);
        return dailyStatRepository.findTopQuery(pageable);
    }
}