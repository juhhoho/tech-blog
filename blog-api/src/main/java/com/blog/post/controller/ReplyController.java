package com.blog.post.controller;

import com.blog.oauth2.jwt.JWTUtil;
import com.blog.post.dto.request.MakeReplyRequest;
import com.blog.post.dto.response.MakeReplyResponse;
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
        log.info("[ReplyController - makeReply] feed_id = {}, feedReplyRequest = {}, username = {}", feedId, makeReplyRequest, jwtUtil.getUsernameFromCookies(request));
        return replyApplicationService.makeReply(feedId, makeReplyRequest.getContent(), jwtUtil.getUsernameFromCookies(request));
    }
}
