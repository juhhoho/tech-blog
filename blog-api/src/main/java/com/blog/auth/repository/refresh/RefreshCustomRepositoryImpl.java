package com.blog.auth.repository.refresh;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
@Transactional
public class RefreshCustomRepositoryImpl implements RefreshCustomRepository{

    private final EntityManager entityManager;

    @Override
    public void deleteByRefresh(String refreshToken) {
        String jpql = "Delete FROM Refresh r WHERE r.refresh = :refreshToken";
        entityManager.createQuery(jpql)
                .setParameter("refreshToken", refreshToken)
                .executeUpdate();
    }

}
