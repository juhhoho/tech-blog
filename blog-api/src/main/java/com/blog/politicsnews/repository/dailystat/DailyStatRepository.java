package com.blog.politicsnews.repository.dailystat;

import com.blog.politicsnews.entity.DailyStat;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DailyStatRepository extends JpaRepository<DailyStat, Long>, DailyStatCustomRepository {

}
