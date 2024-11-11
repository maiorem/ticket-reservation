package com.hhplus.io.app.usertoken.infra;

import com.hhplus.io.app.usertoken.domain.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTokenJpaRepository extends JpaRepository<UserToken, Long> {
    Optional<UserToken> findByUserId(Long userId);

    Optional<UserToken> findByToken(String token);
}
