package com.blog.post.repository.feed;

import com.blog.post.entity.Feed;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class FeedCommandRepositoryImpl implements FeedCommandRepository {

    // em을 사용하면 영속성 컨텍스트 무시하고 바로 db에 반영
    // 따라서 반드시 @Transactional 적용된 컨텍스트에서 호출
    private final EntityManager entityManager;

    @Override
    public void addLikeCount(Feed feed) {
        String jpql = "UPDATE Feed f SET f.likeCount = f.likeCount + 1 WHERE f = :feed";
        entityManager.createQuery(jpql)
                .setParameter("feed", feed)
                .executeUpdate();
    }

    @Override
    public void subLikeCount(Feed feed) {
        String jpql = "UPDATE Feed f SET f.likeCount = f.likeCount - 1 WHERE f = :feed";
        entityManager.createQuery(jpql)
                .setParameter("feed", feed)
                .executeUpdate();
    }
}
