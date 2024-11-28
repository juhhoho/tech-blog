package com.blog.auth.repository;

import com.blog.auth.entity.BaseUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseUserRepository extends JpaRepository<BaseUser, Long> {

    BaseUser findByIdentifier(String identifier);

}
