package com.blog.politicsnews.service;

import com.blog.politicsnews.entity.DailyStat;
import com.blog.politicsnews.repository.dailystat.DailyStatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class DailyStatCommandService {
    private final DailyStatRepository dailyStatRepository;

    @Transactional
    public void save(DailyStat dailyStat){
        log.info("save daily stat: {}", dailyStat);
        dailyStatRepository.save(dailyStat);
    }
}
