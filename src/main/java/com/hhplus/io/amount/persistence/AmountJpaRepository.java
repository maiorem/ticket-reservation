package com.hhplus.io.amount.persistence;

import com.hhplus.io.amount.domain.entity.Amount;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface AmountJpaRepository extends JpaRepository<Amount, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Amount> findByUserId(Long userId);
}
