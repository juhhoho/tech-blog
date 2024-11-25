package com.blog.auth.repository.refresh;

public interface RefreshCustomRepository {
    void deleteByRefresh(String refreshToken);
}
