package com.blog.politicsnews.repository;

import com.blog.politicsnews.dto.response.StatResponse;
import com.blog.politicsnews.entity.DailyStat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;


public interface DailyStatRepository extends JpaRepository<DailyStat, Long> {

    long countByQueryAndEventDateTimeBetween(String query, LocalDateTime start, LocalDateTime end);

    @Query("SELECT new com.blog.politicsnews.dto.response.StatResponse(ds.query, count(ds.query))" +
            "FROM DailyStat ds " +
            "GROUP BY ds.query ORDER BY count(ds.query) DESC")
    List<StatResponse> findTopQuery(Pageable pageable);

}
