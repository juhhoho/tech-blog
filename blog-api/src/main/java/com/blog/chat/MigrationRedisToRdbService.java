package com.blog.chat;

import com.blog.chat.service.RedisServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MigrationRedisToRdbService {

    private final RedisServiceImpl redisService;

    // @Scheduled(cron = "0 0 0 * * ? ") // 매일  00:00 에 실행
    /*
    1. component 즉, 스프링 빈에 등록된 클래스여야함
    2. method는 void type
    3. moethod의 매개변수 사용 불가능
    -> fixedDelay 종료 후 정의된 시간 이후
    -> fixedDRate 시작 후 정의된 시간 이후
     */
    @Scheduled(fixedDelay =  60 * 1000)
    public void migrateChatLogs(){
        log.info("[MigrationRedisToRdb - migrateChatLogs]");
        redisService.migrateChatLogs();
    }

}
