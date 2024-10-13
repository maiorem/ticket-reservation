package com.hhplus.io.user.persistence;

import com.hhplus.io.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    Optional<User> findByUuid(String uuid);
}
