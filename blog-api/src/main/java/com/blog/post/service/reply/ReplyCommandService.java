package com.blog.post.service.reply;

import com.blog.exception.CustomException.NoResourceFoundException;
import com.blog.oauth2.entity.User;
import com.blog.oauth2.repository.UserRepository;
import com.blog.post.dto.response.MakeReplyResponse;
import com.blog.post.entity.Feed;
import com.blog.post.entity.Reply;
import com.blog.post.repository.feed.FeedRepository;
import com.blog.post.repository.reply.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReplyCommandService {
    private final FeedRepository feedRepository;
    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;


    public ResponseEntity<MakeReplyResponse> makeReply(Long feedId, String content, String username) {
        log.info("[ReplyCommandService - makeReply] feedId = {}, content = {}, username ={}", feedId, content, username);

        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new NoResourceFoundException(feedId + "를 id로 갖는 feed를 찾을 수 없습니다."));

        User user = userRepository.findByUserName(username);

        Reply reply = Reply.builder()
                .content(content)
                .feed(feed)
                .user(user)
                .build();

        replyRepository.saveAndFlush(reply);

        MakeReplyResponse makeReplyResponse = MakeReplyResponse.builder()
                .feedId(reply.getFeed().getId())
                .replyId(reply.getId())
                .userId(user.getId())
                .content(reply.getContent())
                .build();


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(makeReplyResponse);


    }
}
