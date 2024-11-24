package com.blog.oauth2.repository.refresh;

public interface RefreshCustomRepository {
    void deleteByRefresh(String refresh);
}
