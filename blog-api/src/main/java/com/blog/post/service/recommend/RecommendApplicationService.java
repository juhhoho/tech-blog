package com.blog.post.service.recommend;

import com.blog.post.dto.response.LikeFeedResponse;
import com.blog.post.dto.response.UnlikeFeedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendApplicationService {
    private final RecommendCommandService recommendCommandService;

    public ResponseEntity<LikeFeedResponse> likeFeed(Long feedId, String username) {
        return recommendCommandService.likeFeed(feedId, username);
    }

    public ResponseEntity<UnlikeFeedResponse> unlikeFeed(Long feedId, String username) {
        return recommendCommandService.unlikeFeed(feedId, username);
    }
}