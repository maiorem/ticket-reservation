package com.hhplus.io.app.amount.domain.repository;

import com.hhplus.io.app.amount.domain.entity.Amount;
import org.springframework.stereotype.Repository;

@Repository
public interface AmountRepository {

    Amount getAmountByUser(Long userId);

    Amount save(Amount amount);

    Amount getAmountByUserWithLock(Long userId);
}
