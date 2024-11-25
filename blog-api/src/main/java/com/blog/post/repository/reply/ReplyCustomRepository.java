package com.blog.post.repository.reply;


public interface ReplyCustomRepository {
    void updateReply(Long replyId, String newContent);
    void deleteReply(Long replyId);
}
