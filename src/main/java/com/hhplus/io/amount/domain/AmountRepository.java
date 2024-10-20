package com.hhplus.io.amount.domain;

import org.springframework.stereotype.Repository;

@Repository
public interface AmountRepository {

    Amount getAmountByUser(Long userId);

    Amount save(Amount amount);
}
