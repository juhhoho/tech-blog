package com.blog.auth.repository.BaseUser;

import com.blog.auth.entity.BaseUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseUserRepository extends JpaRepository<BaseUser, Long>, BaseUserCustomRepository {

    BaseUser findByIdentifier(String identifier);

}
