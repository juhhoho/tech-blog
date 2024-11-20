package com.blog.post.service.reply;

import com.blog.post.dto.response.MakeReplyResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReplyApplicationService {
    private final ReplyCommandService replyCommandService;

    public ResponseEntity<MakeReplyResponse> makeReply(Long feedId, String content, String username) {
        return replyCommandService.makeReply(feedId, content, username);
    }
}
