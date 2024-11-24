package com.blog.post.repository.feed;

import com.blog.post.entity.Feed;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;


@RequiredArgsConstructor
@Repository
public class FeedCustomRepositoryImpl implements FeedCustomRepository {

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

    @Override
    public void updateFeedTitleDescriptionLastBuildTime(Feed feed , String title, String description, LocalDateTime lastBuildTime) {
        String jpql = "UPDATE Feed f SET f.title = :title, f.description = :description, f.lastBuildTime = :lastBuildTime WHERE f = :feed";
        entityManager.createQuery(jpql)
                .setParameter("feed", feed)
                .setParameter("title", title)
                .setParameter("description", description)
                .setParameter("lastBuildTime", lastBuildTime)
                .executeUpdate();
    }

}
