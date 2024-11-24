package com.blog.oauth2.repository.refresh;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class RefreshCustomRepositoryImpl implements RefreshCustomRepository{

    private final EntityManager entityManager;

    @Override
    public void deleteByRefresh(String refresh) {
        String jpql = "Delete FROM Refresh r WHERE r = :refresh";
        entityManager.createQuery(jpql)
                .setParameter("refresh", refresh)
                .executeUpdate();
    }

}
