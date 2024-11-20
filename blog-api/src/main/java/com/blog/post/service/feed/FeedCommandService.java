package com.blog.post.service.feed;

import com.blog.oauth2.entity.User;
import com.blog.oauth2.repository.UserRepository;
import com.blog.post.dto.response.MakeBlogFeedResponse;
import com.blog.post.entity.Feed;
import com.blog.post.repository.feed.FeedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FeedCommandService {
    private final FeedRepository feedRepository;
    private final UserRepository userRepository;

    public ResponseEntity<MakeBlogFeedResponse> makeBlogFeed(String title, String description, String username) {
        log.info("[FeedCommandService - makeBlogFeed] title = {}, description = {}, username ={}", title, description, username);

        User user = userRepository.findByUserName(username);

        Feed newFeed = Feed.builder()
                .title(title)
                .description(description)
                .lastBuildTime(LocalDateTime.now())
                .user(user)
                .likeCount(0)
                .build();
        Feed savedFeed = feedRepository.saveAndFlush(newFeed);

        MakeBlogFeedResponse makeBlogFeedResponse = MakeBlogFeedResponse.builder()
                .id(savedFeed.getId())
                .title(savedFeed.getTitle())
                .description(savedFeed.getDescription())
                .lastBuildTime(savedFeed.getLastBuildTime())
                .userId(user.getId())
                .likeCount(savedFeed.getLikeCount())
                .replies(new ArrayList<>())
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(makeBlogFeedResponse);

    }
}
