package com.hhplus.io.amount.persistence;

import com.hhplus.io.amount.domain.Amount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AmountJpaRepository extends JpaRepository<Amount, Long> {
    Optional<Amount> findByUserId(Long userId);
}
