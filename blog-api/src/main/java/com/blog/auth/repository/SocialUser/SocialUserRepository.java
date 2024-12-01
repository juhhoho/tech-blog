package com.blog.auth.repository.SocialUser;

import com.blog.auth.entity.SocialUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface SocialUserRepository extends JpaRepository<SocialUser, Long>, SocialUserCustomRepository {

    Optional<SocialUser> findByIdentifier(String identifier);
}
