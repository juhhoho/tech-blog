package com.blog.post.service.stat;

import com.blog.exception.CustomException.NoResourceFoundException;
import com.blog.post.entity.Feed;
import com.blog.post.entity.StatDailyLike;
import com.blog.post.repository.feed.FeedRepository;
import com.blog.post.repository.stat.StatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class StatCommandService {
    private final StatRepository statRepository;
    private final FeedRepository feedRepository;

    public void saveLikeTime(Long feedId, LocalDateTime likeDateTime){
        log.info("[StatCommandService] saveLikeTime - feedId: {}, likeDateTime: {}", feedId, likeDateTime);

        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new NoResourceFoundException(feedId + "를 id로 갖는 feed를 찾을 수 없습니다."));

        StatDailyLike statDailyLike = StatDailyLike.builder()
                .feed(feed)
                .likeDateTime(likeDateTime)
                .build();

        statRepository.save(statDailyLike);
    }
}
