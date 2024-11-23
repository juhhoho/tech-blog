package com.blog.oauth2.repository;

import com.blog.oauth2.entity.BaseUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseUserRepository extends JpaRepository<BaseUser, Long> {

    BaseUser findByIdentifier(String identifier);

}
