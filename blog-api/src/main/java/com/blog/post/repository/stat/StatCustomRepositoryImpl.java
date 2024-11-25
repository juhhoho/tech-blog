package com.blog.post.repository.stat;

import com.blog.post.entity.Feed;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class StatCustomRepositoryImpl implements StatCustomRepository{

    private final EntityManager entityManager;

    @Override
    public List<Feed> getNFeedsByMostLiked(int count) {
        String jpql = "SELECT f FROM Feed f ORDER BY f.likeCount DESC"; // likeCount 기준 내림차순 정렬
        return entityManager.createQuery(jpql, Feed.class)
                .setMaxResults(count) // 반환할 최대 개수 설정
                .getResultList(); // 결과 가져오기
    }

    // 특정 date애 대해 그날의 추천수에 따른 n개의 feed를 반환
    @Override
    public List<Feed> getNFeedsByMostDailyLiked(int count, LocalDate date) {
        String jpql = "SELECT f " +
                "FROM Feed f " +
                "JOIN StatDailyLike sdl ON sdl.feed = f " +
                "WHERE sdl.likeDateTime BETWEEN :startOfDay AND :endOfDay " +
                "GROUP BY f " +
                "ORDER BY COUNT(sdl.id) DESC";

        LocalDateTime startOfDay = date.atStartOfDay(); // 00:00:00
        LocalDateTime endOfDay = date.atTime(23, 59, 59); // 23:59:59

        return entityManager.createQuery(jpql, Feed.class)
                .setParameter("startOfDay", startOfDay) // 하루의 시작
                .setParameter("endOfDay", endOfDay) // 하루의 끝
                .setMaxResults(count) // 반환할 최대 개수 설정
                .getResultList();
    }

    @Override
    public List<Feed> getNFeedsByMostDisliked(int count) {
        String jpql = "SELECT f FROM Feed f ORDER BY f.dislikeCount DESC"; // dislikeCount 기준 내림차순 정렬
        return entityManager.createQuery(jpql, Feed.class)
                .setMaxResults(count) // 반환할 최대 개수 설정
                .getResultList(); // 결과 가져오기
    }

    @Override
    public List<Feed> getNFeedsByMostDailyDisliked(int count, LocalDate date) {
        String jpql = "SELECT f " +
                "FROM Feed f " +
                "JOIN StatDailyDislike sddl ON sddl.feed = f " +
                "WHERE sddl.dislikeDateTime BETWEEN :startOfDay AND :endOfDay " +
                "GROUP BY f " +
                "ORDER BY COUNT(sddl.id) DESC";

        LocalDateTime startOfDay = date.atStartOfDay(); // 00:00:00
        LocalDateTime endOfDay = date.atTime(23, 59, 59); // 23:59:59

        return entityManager.createQuery(jpql, Feed.class)
                .setParameter("startOfDay", startOfDay) // 하루의 시작
                .setParameter("endOfDay", endOfDay) // 하루의 끝
                .setMaxResults(count) // 반환할 최대 개수 설정
                .getResultList();
    }

    @Override
    public List<Feed> getNFeedsByMostViewed(int count) {
        String jpql = "SELECT f FROM Feed f ORDER BY f.viewCount DESC"; // viewCount 기준 내림차순 정렬
        return entityManager.createQuery(jpql, Feed.class)
                .setMaxResults(count) // 반환할 최대 개수 설정
                .getResultList(); // 결과 가져오기
    }


}
