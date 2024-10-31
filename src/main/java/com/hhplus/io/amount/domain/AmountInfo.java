package com.hhplus.io.amount.domain;

import com.hhplus.io.amount.domain.entity.Amount;
import com.hhplus.io.amount.domain.repository.AmountRepository;
import com.hhplus.io.support.domain.error.CoreException;
import com.hhplus.io.support.domain.error.ErrorType;
import jakarta.transaction.Transactional;
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
        if (updateAmount < 0) {
            throw new CoreException(ErrorType.FORBIDDEN);
        }
        Amount amount = amountRepository.getAmountByUserWithLock(userId);
        amount.saveAmount(updateAmount);
        return amount.getAmount();
    }

    @Transactional
    public void payAmount(Long userId, int payment) {
        Amount amount = amountRepository.getAmountByUserWithLock(userId);
        if (amount.getAmount() - payment < 0) {
            throw new CoreException(ErrorType.FORBIDDEN);
        }
        amount.payAmount(payment);
        amount.updatePaymentDate(LocalDateTime.now());
    }
}