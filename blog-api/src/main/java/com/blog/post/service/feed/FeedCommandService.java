package com.blog.post.service.feed;

import com.blog.exception.CustomException.NoResourceFoundException;
import com.blog.oauth2.entity.User;
import com.blog.oauth2.repository.UserRepository;
import com.blog.post.dto.response.MakeBlogFeedResponse;
import com.blog.post.entity.Feed;
import com.blog.post.repository.feed.FeedRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    public void viewCountUp(Long feedId, HttpServletRequest req, HttpServletResponse res){
        log.info("[FeedCommandService - viewCountUp]");
        Cookie oldCookie = null;

        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("feedView")) {
                    oldCookie = cookie;
                }
            }
        }

        if (oldCookie != null) {
            // 다른 feed에 대해 쿠키 정보가 있는 경우 -> 이번에 조회한 feed에 대해 쿠키 업데이트
            if (!oldCookie.getValue().contains("[" + feedId.toString() + "]")) {
                Feed feed = feedRepository.findById(feedId).orElseThrow(
                        ()-> new NoResourceFoundException(feedId + "를 id로 갖는 feed를 찾을 수 없습니다."));

                Feed.viewCountUp(feed);
                oldCookie.setValue(oldCookie.getValue() + "_[" + feedId + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60 * 24);
                res.addCookie(oldCookie);
                // 업데이트된 쿠키 정보를 로그로 출력
                log.info("[FeedCommandService - viewCountUp] updated cookie - Name: {}, Value: {}", oldCookie.getName(), oldCookie.getValue());
            }
        } else {
            // 쿠키 정보 자체가 아예 없는 경우 -> 쿠키 생성
            Feed feed = feedRepository.findById(feedId).orElseThrow(
                    ()-> new NoResourceFoundException(feedId + "를 id로 갖는 feed를 찾을 수 없습니다."));

            Feed.viewCountUp(feed);
            Cookie newCookie = new Cookie("feedView","[" + feedId + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24);
            res.addCookie(newCookie);
            // 생성된 쿠키 정보를 로그로 출력
            log.info("[FeedCommandService - viewCountUp] created new cookie - Name: {}, Value: {}", newCookie.getName(), newCookie.getValue());
        }
    }
}
