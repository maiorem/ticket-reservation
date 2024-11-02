package com.hhplus.io.amount.service;

import com.hhplus.io.amount.domain.entity.Amount;
import com.hhplus.io.amount.domain.AmountInfo;
import com.hhplus.io.support.domain.aop.DistributedLock;
import jakarta.transaction.Transactional;
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

    @Transactional
    @DistributedLock(key = "#userId.concat('-').concat(#payment)")
    public void pay(Long userId, int payment) {
        amountInfo.payAmount(userId, payment);
    }
}
