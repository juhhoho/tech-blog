package com.blog.politicsnews.repository.dailystat;

import com.blog.politicsnews.dto.response.StatResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface DailyStatCustomRepository {
    long getQueryCountByDate(String query, LocalDateTime start, LocalDateTime end);

    List<StatResponse> findTopQuery(Pageable pageable);
}
