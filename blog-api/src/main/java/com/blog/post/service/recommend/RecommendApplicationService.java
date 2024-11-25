package com.blog.post.service.recommend;

import com.blog.post.dto.response.DislikeFeedResponse;
import com.blog.post.dto.response.LikeFeedResponse;
import com.blog.post.dto.response.UnDislikeFeedResponse;
import com.blog.post.dto.response.UnlikeFeedResponse;
import com.blog.post.service.stat.StatCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendApplicationService {
    private final RecommendCommandService recommendCommandService;
    private final StatCommandService statCommandService;

    public ResponseEntity<LikeFeedResponse> likeFeed(Long feedId, String username, LocalDateTime likeDateTime) {
        // 일간 추천수 집계
        statCommandService.saveLikeTime(feedId, likeDateTime);

        // 추천
        return recommendCommandService.likeFeed(feedId, username);
    }

    public ResponseEntity<DislikeFeedResponse> dislikeFeed(Long feedId, String username, LocalDateTime dislikeDateTime) {
        // 일간 비추천수 집계
        statCommandService.saveDislikeTime(feedId, dislikeDateTime);

        // 추천
        return recommendCommandService.dislikeFeed(feedId, username);
    }

    public ResponseEntity<UnlikeFeedResponse> unlikeFeed(Long feedId, String username) {
        return recommendCommandService.unlikeFeed(feedId, username);
    }

    public ResponseEntity<UnDislikeFeedResponse> unDislikeFeed(Long feedId, String username) {
        return recommendCommandService.unDislikeFeed(feedId, username);
    }
}