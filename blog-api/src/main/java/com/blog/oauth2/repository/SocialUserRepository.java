package com.blog.oauth2.repository;

import com.blog.oauth2.entity.LocalUser;
import com.blog.oauth2.entity.SocialUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface SocialUserRepository extends JpaRepository<SocialUser, Long> {

    boolean existsByIdentifier(String identifier);

    Optional<SocialUser> findSocialUserByIdentifier(String identifier);
}
