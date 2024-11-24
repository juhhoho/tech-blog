package com.blog.oauth2.repository.user;

import com.blog.oauth2.entity.LocalUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocalUserRepository extends JpaRepository<LocalUser, Long> {

    boolean existsByIdentifier(String identifier);

    boolean existsByEmail(String email);

    Optional<LocalUser> findLocalUserByIdentifier(String identifier);
}
