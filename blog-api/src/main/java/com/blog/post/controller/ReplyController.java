package com.blog.post.controller;

import com.blog.auth.jwt.JWTUtil;
import com.blog.post.dto.request.MakeReplyRequest;
import com.blog.post.dto.request.UpdateReplyRequest;
import com.blog.post.dto.response.DeleteReplyResponse;
import com.blog.post.dto.response.MakeReplyResponse;
import com.blog.post.dto.response.UpdateReplyResponse;
import com.blog.post.service.reply.ReplyApplicationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/blog")
@Slf4j
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyApplicationService replyApplicationService;
    private final JWTUtil jwtUtil;

    //------------------------------------------------------------------------------------------------------------------
    // <POST>
    //------------------------------------------------------------------------------------------------------------------

    // reply 작성
    @PostMapping("/feeds/{feed_id}/reply")
    public ResponseEntity<MakeReplyResponse> makeReply(
            @PathVariable("feed_id") Long feedId,
            @Valid @RequestBody MakeReplyRequest makeReplyRequest,
            HttpServletRequest request)
    {
        log.info("[ReplyController - makeReply] feed_id = {}, feedReplyRequest = {}, identifier = {}", feedId, makeReplyRequest, jwtUtil.getIdentifierFromHttpRequest(request));
        return replyApplicationService.makeReply(feedId, makeReplyRequest.getContent(), jwtUtil.getIdentifierFromHttpRequest(request));
    }

    // reply 수정
    @PatchMapping("/feeds/{feed_id}/reply/{reply_id}")
    public ResponseEntity<UpdateReplyResponse> updateReply(
            @PathVariable("feed_id") Long feedId,
            @PathVariable("reply_id") Long replyId,
            @Valid @RequestBody UpdateReplyRequest UpdateReplyRequest,
            HttpServletRequest request)
    {
        log.info("[ReplyController - updateReply] feed_id = {}, reply_id = {}, UpdateReplyRequest = {}, identifier = {}", feedId, replyId, UpdateReplyRequest, jwtUtil.getIdentifierFromHttpRequest(request));
        return replyApplicationService.updateReply(feedId, replyId, UpdateReplyRequest, jwtUtil.getIdentifierFromHttpRequest(request));
    }

    // reply 삭제
    @DeleteMapping("/feeds/{feed_id}/reply/{reply_id}")
    public ResponseEntity<DeleteReplyResponse> deleteReply(
            @PathVariable("feed_id") Long feedId,
            @PathVariable("reply_id") Long replyId,
            HttpServletRequest request)
    {
        log.info("[ReplyController - deleteReply] feed_id = {}, replyId = {}, identifier = {}", feedId, replyId, jwtUtil.getIdentifierFromHttpRequest(request));
        return replyApplicationService.deleteReply(feedId, replyId, jwtUtil.getIdentifierFromHttpRequest(request));
    }
}
