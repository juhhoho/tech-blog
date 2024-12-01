package com.blog.politicsnews.repository.dailystat;

import com.blog.politicsnews.dto.response.StatResponse;
import com.blog.politicsnews.entity.QDailyStat;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class DailyStatCustomRepositoryImpl implements DailyStatCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public long getQueryCountByDate(String query, LocalDateTime start, LocalDateTime end) {
        QDailyStat dailyStat = QDailyStat.dailyStat;

        return Optional.ofNullable(
                jpaQueryFactory.select(dailyStat.count())
                .from(dailyStat)
                .where(
                        dailyStat.query.eq(query),
                        dailyStat.eventDateTime.between(start, end)
                )
                .fetchOne()
                )
                .orElse(0L);
    }

    @Override
    public List<StatResponse> findTopQuery(Pageable pageable) {
        QDailyStat dailyStat = QDailyStat.dailyStat;

        return jpaQueryFactory
                .select(Projections.constructor(
                        StatResponse.class,
                        dailyStat.query,
                        dailyStat.query.count()
                ))
                .from(dailyStat)
                .groupBy(dailyStat.query)
                .orderBy(dailyStat.query.count().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
