package com.hhplus.io.amount.domain;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class AmountInfo {

    private final AmountRepository amountRepository;

    public AmountInfo(AmountRepository amountRepository) {
        this.amountRepository = amountRepository;
    }

    public Amount getAmountByUser(Long userId) {
        return amountRepository.getAmountByUser(userId);
    }

    public int updateAmount(Long userId, int updateAmount) {
        Amount amount = getAmountByUser(userId);
        amount.saveAmount(updateAmount);
        return amount.getAmount();
    }

    public void payAmount(Long userId, int payment) {
        Amount amount = getAmountByUser(userId);
        amount.payAmount(payment);
        amount.updatePaymentDate(LocalDateTime.now(ZoneId.of("Asia/Seoul")));

    }
}
