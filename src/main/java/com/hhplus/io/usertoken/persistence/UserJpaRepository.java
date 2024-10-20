package com.hhplus.io.usertoken.persistence;

import com.hhplus.io.usertoken.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(Long userId);
}
