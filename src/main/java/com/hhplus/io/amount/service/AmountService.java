package com.hhplus.io.amount.service;

import com.hhplus.io.amount.domain.Amount;
import com.hhplus.io.amount.domain.AmountInfo;
import org.springframework.stereotype.Service;

@Service
public class AmountService {

    private final AmountInfo amountInfo;

    public AmountService(AmountInfo amountInfo) {
        this.amountInfo = amountInfo;
    }

    public Amount getAmountByUser(Long userId) {
        return amountInfo.getAmountByUser(userId);
    }

    public int updateAmount(Long userId, int updateAmount) {
        return amountInfo.updateAmount(userId, updateAmount);
    }

    public void pay(Long userId, int payment) {
        amountInfo.payAmount(userId, payment);
    }
}
