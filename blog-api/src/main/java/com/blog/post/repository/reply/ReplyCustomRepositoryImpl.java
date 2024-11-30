package com.blog.post.repository.reply;

import com.blog.post.entity.QReply;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
public class ReplyCustomRepositoryImpl implements ReplyCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    @Transactional
    public void updateReply(Long replyId, String newContent) {
        jpaQueryFactory.update(QReply.reply)
                .set(QReply.reply.content, newContent)
                .where(QReply.reply.id.eq(replyId))
                .execute();
    }

    @Override
    @Transactional
    public void deleteReply(Long replyId) {
        jpaQueryFactory.delete(QReply.reply)
                .where(QReply.reply.id.eq(replyId))
                .execute();
    }
}
