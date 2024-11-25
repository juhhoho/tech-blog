package com.blog.auth.repository.refresh;

import com.blog.auth.entity.Refresh;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshRepository extends JpaRepository<Refresh, Long> {
    Boolean existsByRefresh(String refresh);

    Optional<Refresh> findByRefresh(String refresh);


}
