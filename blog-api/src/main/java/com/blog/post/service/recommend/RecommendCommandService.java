package com.blog.post.service.recommend;

import com.blog.exception.CustomException.NoResourceFoundException;
import com.blog.exception.CustomException.RecommendException;
import com.blog.oauth2.entity.User;
import com.blog.oauth2.repository.UserRepository;
import com.blog.post.dto.response.LikeFeedResponse;
import com.blog.post.dto.response.UnlikeFeedResponse;
import com.blog.post.entity.Feed;
import com.blog.post.entity.Recommend;
import com.blog.post.repository.feed.FeedCommandRepository;
import com.blog.post.repository.feed.FeedRepository;
import com.blog.post.repository.recommend.RecommendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RecommendCommandService {
    private final RecommendRepository recommendRepository;
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;

    @Qualifier("feedCommandRepositoryImpl")
    private final FeedCommandRepository feedCommandRepository;

    public ResponseEntity<LikeFeedResponse> likeFeed(Long feedId, String username){
        log.info("[RecommendCommandService - likeFeed] feedId = {}, username = {}", feedId, username);

        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new NoResourceFoundException(feedId + "를 id 값으로 갖는 feed를 찾을 수 없습니다.")
        );

        User user = Optional.of(userRepository.findByUserName(username)).orElseThrow(
                () -> new NoResourceFoundException(username + "을 username 값으로 갖는 user를 찾을 수 없습니다.")
        );

        // 이미 like -> error 처리
        if(recommendRepository.findByUserAndFeed(user, feed).isPresent()){
            throw new RecommendException("동일한 feed에 중복해서 추천을 누를 수 없습니다.");
        }
        Recommend recommend = Recommend.builder()
                .user(user)
                .feed(feed)
                .build();

        recommendRepository.saveAndFlush(recommend);

        // feed에 대해 count + 1, 중복 x
        feedCommandRepository.addLikeCount(feed);

        LikeFeedResponse likeFeedResponse = LikeFeedResponse.builder()
                .userId(user.getId())
                .feedId(feed.getId())
                .likeCount(feed.getLikeCount())
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(likeFeedResponse);
    }

    public ResponseEntity<UnlikeFeedResponse> unlikeFeed(Long feedId, String username){
        log.info("[RecommendCommandService - unlikeFeed] feedId = {}, username = {}", feedId, username);

        Feed feed = feedRepository.findById(feedId).orElseThrow(
                 () -> new NoResourceFoundException(feedId + "를 id 값으로 갖는 feed를 찾을 수 없습니다.")
        );

        User user = Optional.of(userRepository.findByUserName(username)).orElseThrow(
                () -> new NoResourceFoundException(username + "을 username 값으로 갖는 user를 찾을 수 없습니다.")
        );
        Recommend recommend = recommendRepository.findByUserAndFeed(user, feed).orElseThrow(
                ()-> new NoResourceFoundException(username + "을 username으로 갖는 user는 " + feedId + "를 id값으로 갖는 feed에 추천을 누르지 않았습니다.")
        );

        recommendRepository.delete(recommend);

        // feed에 대해 count - 1, 중복 x
        feedCommandRepository.subLikeCount(feed);

        UnlikeFeedResponse unlikeFeedResponse = UnlikeFeedResponse.builder()
                .userId(user.getId())
                .feedId(feed.getId())
                .likeCount(feed.getLikeCount())
                .build();

        return ResponseEntity
                .ok()
                .body(unlikeFeedResponse);
    }
    
}
