package com.blog.post.repository.reply;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
@Transactional
public class ReplyCustomRepositoryImpl implements ReplyCustomRepository{

    private final EntityManager entityManager;

    @Override
    public void updateReply(Long replyId, String newContent) {
        String jpql = "UPDATE Reply r SET r.content = :newContent WHERE r.id = :replyId";
        entityManager.createQuery(jpql)
                .setParameter("replyId", replyId)
                .setParameter("newContent", newContent)
                .executeUpdate();
    }

    @Override
    public void deleteReply(Long replyId) {
        String jpql = "DELETE FROM Reply r WHERE r.id = :replyId";
        entityManager.createQuery(jpql)
                .setParameter("replyId", replyId)
                .executeUpdate();
    }
}
