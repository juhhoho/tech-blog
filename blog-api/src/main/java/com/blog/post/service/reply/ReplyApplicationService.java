package com.blog.post.service.reply;

import com.blog.post.dto.request.UpdateReplyRequest;
import com.blog.post.dto.response.DeleteReplyResponse;
import com.blog.post.dto.response.MakeReplyResponse;
import com.blog.post.dto.response.UpdateReplyResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReplyApplicationService {
    private final ReplyCommandService replyCommandService;

    public ResponseEntity<MakeReplyResponse> makeReply(Long feedId, String content, String username) {
        return replyCommandService.makeReply(feedId, content, username);
    }

    public ResponseEntity<UpdateReplyResponse> updateReply(Long feedId, Long replyId, UpdateReplyRequest updateReplyRequest,String identifier) {
        return replyCommandService.updateReply(feedId, replyId, updateReplyRequest, identifier);
    }

    public ResponseEntity<DeleteReplyResponse> deleteReply(Long feedId, Long replyId, String identifier){
        return replyCommandService.deleteReply(feedId, replyId, identifier);
    }
}
