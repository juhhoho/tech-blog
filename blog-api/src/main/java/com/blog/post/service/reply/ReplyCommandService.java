package com.blog.post.service.reply;

import com.blog.exception.CustomException.NoResourceFoundException;
import com.blog.oauth2.entity.BaseUser;
import com.blog.oauth2.repository.BaseUserRepository;
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
    private final BaseUserRepository baseUserRepository;


    public ResponseEntity<MakeReplyResponse> makeReply(Long feedId, String content, String identifier) {
        log.info("[ReplyCommandService - makeReply] feedId = {}, content = {}, identifier ={}", feedId, content, identifier);

        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new NoResourceFoundException(feedId + "를 id로 갖는 feed를 찾을 수 없습니다."));

        BaseUser user = baseUserRepository.findByIdentifier(identifier);

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
