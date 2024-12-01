package com.blog.auth.repository.LocalUser;

import com.blog.auth.entity.LocalUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocalUserRepository extends JpaRepository<LocalUser, Long>, LocalUserCustomRepository {

    boolean existsByIdentifier(String identifier);

    boolean existsByEmail(String email);

    Optional<LocalUser> findByIdentifier(String identifier);
}
