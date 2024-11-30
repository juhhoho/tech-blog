package com.blog.post.service.recommend;

import com.blog.exception.CustomException.NoResourceFoundException;
import com.blog.exception.CustomException.RecommendException;
import com.blog.auth.entity.BaseUser;
import com.blog.auth.repository.BaseUserRepository;
import com.blog.post.dto.response.DislikeFeedResponse;
import com.blog.post.dto.response.LikeFeedResponse;
import com.blog.post.dto.response.UnDislikeFeedResponse;
import com.blog.post.dto.response.UnlikeFeedResponse;
import com.blog.post.entity.Feed;
import com.blog.post.entity.Recommend;
import com.blog.post.repository.feed.FeedRepository;
import com.blog.post.repository.recommend.RecommendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final BaseUserRepository baseUserRepository;
    private final FeedRepository feedRepository;

    public ResponseEntity<LikeFeedResponse> likeFeed(Long feedId, String identifier){
        log.info("[RecommendCommandService - likeFeed] feedId = {}, identifier = {}", feedId, identifier);
        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new NoResourceFoundException(feedId + "를 id 값으로 갖는 feed를 찾을 수 없습니다.")
        );

        BaseUser user = Optional.of(baseUserRepository.findByIdentifier(identifier)).orElseThrow(
                () -> new NoResourceFoundException(identifier + "을 identifier 값으로 갖는 user를 찾을 수 없습니다.")
        );

        // 이미 like -> error 처리
        if(recommendRepository.checkDuplicate(user, feed, "like").isPresent()){
            throw new RecommendException("동일한 feed에 중복해서 추천을 누를 수 없습니다.");
        }
        Recommend recommend = Recommend.builder()
                .user(user)
                .feed(feed)
                .type("like")
                .build();

        recommendRepository.saveAndFlush(recommend);

        // feed에 대해 count + 1, 중복 x
        feedRepository.addLikeCount(feed);
        LikeFeedResponse likeFeedResponse = LikeFeedResponse.builder()
                .userId(user.getId())
                .feedId(feed.getId())
                .likeCount(feed.getLikeCount() + 1)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(likeFeedResponse);
    }

    public ResponseEntity<DislikeFeedResponse> dislikeFeed(Long feedId, String identifier){
        log.info("[RecommendCommandService - dislikeFeed] feedId = {}, identifier = {}", feedId, identifier);

        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new NoResourceFoundException(feedId + "를 id 값으로 갖는 feed를 찾을 수 없습니다.")
        );

        BaseUser user = Optional.of(baseUserRepository.findByIdentifier(identifier)).orElseThrow(
                () -> new NoResourceFoundException(identifier + "을 identifier 값으로 갖는 user를 찾을 수 없습니다.")
        );

        // 이미 dislike -> error 처리
        if(recommendRepository.checkDuplicate(user, feed, "dislike").isPresent()){
            throw new RecommendException("동일한 feed에 중복해서 비추천을 누를 수 없습니다.");
        }
        Recommend recommend = Recommend.builder()
                .user(user)
                .feed(feed)
                .type("dislike")
                .build();

        recommendRepository.saveAndFlush(recommend);

        // feed에 대해 count + 1, 중복 x
        feedRepository.addDislikeCount(feed);


        DislikeFeedResponse dislikeFeedResponse = DislikeFeedResponse.builder()
                .userId(user.getId())
                .feedId(feed.getId())
                .dislikeCount(feed.getDislikeCount() + 1)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(dislikeFeedResponse);
    }

    public ResponseEntity<UnlikeFeedResponse> unlikeFeed(Long feedId, String identifier){
        log.info("[RecommendCommandService - unlikeFeed] feedId = {}, identifier = {}", feedId, identifier);

        Feed feed = feedRepository.findById(feedId).orElseThrow(
                 () -> new NoResourceFoundException(feedId + "를 id 값으로 갖는 feed를 찾을 수 없습니다.")
        );

        BaseUser user = Optional.of(baseUserRepository.findByIdentifier(identifier)).orElseThrow(
                () -> new NoResourceFoundException(identifier + "을 identifier 값으로 갖는 user를 찾을 수 없습니다.")
        );

        Recommend recommend = recommendRepository.checkDuplicate(user, feed, "like").orElseThrow(
                ()-> new NoResourceFoundException(identifier + "을 identifier으로 갖는 user는 " + feedId + "를 id값으로 갖는 feed에 추천을 누르지 않았습니다.")
        );

        recommendRepository.delete(recommend);

        // feed에 대해 count - 1, 중복 x
        feedRepository.subLikeCount(feed);

        UnlikeFeedResponse unlikeFeedResponse = UnlikeFeedResponse.builder()
                .userId(user.getId())
                .feedId(feed.getId())
                .likeCount(feed.getLikeCount() - 1)
                .build();

        return ResponseEntity
                .ok()
                .body(unlikeFeedResponse);
    }

    public ResponseEntity<UnDislikeFeedResponse> unDislikeFeed(Long feedId, String identifier){
        log.info("[RecommendCommandService - unlikeFeed] feedId = {}, identifier = {}", feedId, identifier);

        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new NoResourceFoundException(feedId + "를 id 값으로 갖는 feed를 찾을 수 없습니다.")
        );

        BaseUser user = Optional.of(baseUserRepository.findByIdentifier(identifier)).orElseThrow(
                () -> new NoResourceFoundException(identifier + "을 identifier 값으로 갖는 user를 찾을 수 없습니다.")
        );

        Recommend recommend = recommendRepository.checkDuplicate(user, feed, "dislike").orElseThrow(
                ()-> new NoResourceFoundException(identifier + "을 identifier으로 갖는 user는 " + feedId + "를 id값으로 갖는 feed에 비추천을 누르지 않았습니다.")
        );


        recommendRepository.delete(recommend);

        // feed에 대해 count - 1, 중복 x
        feedRepository.subDislikeCount(feed);

        UnDislikeFeedResponse unDislikeFeedResponse = UnDislikeFeedResponse.builder()
                .userId(user.getId())
                .feedId(feed.getId())
                .dislikeCount(feed.getDislikeCount() - 1)
                .build();

        return ResponseEntity
                .ok()
                .body(unDislikeFeedResponse);
    }
    
}
