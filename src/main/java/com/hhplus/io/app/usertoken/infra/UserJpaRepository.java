package com.hhplus.io.app.usertoken.infra;

import com.hhplus.io.app.usertoken.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(Long userId);
}
