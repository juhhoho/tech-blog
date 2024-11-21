package com.blog.post.repository.feed;

import com.blog.post.entity.Feed;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    @Override
    public List<Feed> getAllNFeedsByMostLiked(int count) {
        String jpql = "SELECT f FROM Feed f ORDER BY f.likeCount DESC"; // likeCount 기준 내림차순 정렬
        return entityManager.createQuery(jpql, Feed.class)
                .setMaxResults(count) // 반환할 최대 개수 설정
                .getResultList(); // 결과 가져오기
    }

    @Override
    public List<Feed> getAllNFeedsByMostViewed(int count) {
        String jpql = "SELECT f FROM Feed f ORDER BY f.viewCount DESC"; // viewCount 기준 내림차순 정렬
        return entityManager.createQuery(jpql, Feed.class)
                .setMaxResults(count) // 반환할 최대 개수 설정
                .getResultList(); // 결과 가져오기
    }
}
