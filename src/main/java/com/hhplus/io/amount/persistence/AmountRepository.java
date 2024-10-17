package com.hhplus.io.amount.persistence;

import com.hhplus.io.amount.domain.entity.Amount;
import org.springframework.stereotype.Repository;

@Repository
public interface AmountRepository {

    Amount getAmountByUser(Long userId);

    Amount save(Amount amount);
}
