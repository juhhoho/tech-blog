package com.blog.post.service.reply;

import com.blog.exception.CustomException.ForbiddenAccessException;
import com.blog.exception.CustomException.NoResourceFoundException;
import com.blog.auth.entity.BaseUser;
import com.blog.auth.repository.BaseUser.BaseUserRepository;
import com.blog.post.dto.request.UpdateReplyRequest;
import com.blog.post.dto.response.DeleteReplyResponse;
import com.blog.post.dto.response.MakeReplyResponse;
import com.blog.post.dto.response.UpdateReplyResponse;
import com.blog.post.entity.Feed;
import com.blog.post.entity.Reply;
import com.blog.post.repository.feed.FeedRepository;
import com.blog.post.repository.reply.ReplyCustomRepository;
import com.blog.post.repository.reply.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReplyCommandService {
    private final FeedRepository feedRepository;
    private final ReplyRepository replyRepository;
    private final ReplyCustomRepository replyCustomRepository;
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

    public ResponseEntity<UpdateReplyResponse> updateReply(Long feedId, Long replyId, UpdateReplyRequest updateReplyRequest, String identifier) {
        log.info("[ReplyCommandService - updateReply] feedId = {}, replyId = {}, UpdateReplyRequest = {}, identifier ={}", feedId, replyId, updateReplyRequest, identifier);

        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new NoResourceFoundException(feedId + "를 id로 갖는 feed를 찾을 수 없습니다."));

        Reply oldReply = replyRepository.findById(replyId).orElseThrow(
                () -> new NoResourceFoundException(replyId + "를 id로 갖는 reply를 찾을 수 없습니다."));

        if(!Objects.equals(oldReply.getUser().getIdentifier(), identifier)){
            throw new ForbiddenAccessException("해당 reply에 대한 수정 권한이 없는 사용자입니다.");
        }

        // 업데이트
        replyCustomRepository.updateReply(replyId, updateReplyRequest.getContent());

        UpdateReplyResponse response = UpdateReplyResponse.builder()
                .feedId(feedId)
                .replyId(replyId)
                .userId(oldReply.getUser().getId())
                .newContent(updateReplyRequest.getContent())
                .build();

        return ResponseEntity
                .ok()
                .body(response);
    }

    public ResponseEntity<DeleteReplyResponse> deleteReply(Long feedId, Long replyId, String identifier){
        log.info("[ReplyCommandService - deleteReply] feedId = {}, replyId = {}, identifier ={}", feedId, replyId, identifier);

        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new NoResourceFoundException(feedId + "를 id로 갖는 feed를 찾을 수 없습니다."));

        Reply oldReply = replyRepository.findById(replyId).orElseThrow(
                () -> new NoResourceFoundException(replyId + "를 id로 갖는 reply를 찾을 수 없습니다."));

        if(!Objects.equals(oldReply.getUser().getIdentifier(), identifier)){
            throw new ForbiddenAccessException("해당 reply에 대한 삭제 권한이 없는 사용자입니다.");
        }

        // 삭제
        replyCustomRepository.deleteReply(replyId);

        DeleteReplyResponse response = DeleteReplyResponse.builder()
                .replyId(replyId)
                .build();

        return ResponseEntity
                .ok()
                .body(response);
    }
}
