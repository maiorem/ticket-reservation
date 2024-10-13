package com.hhplus.io.user.persistence;

import com.hhplus.io.user.domain.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTokenJpaRepository extends JpaRepository<UserToken, Long> {
    Optional<UserToken> findByUserId(Long userId);
}
