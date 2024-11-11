package com.hhplus.io.app.amount.domain;

import com.hhplus.io.app.amount.domain.entity.Amount;
import com.hhplus.io.app.amount.domain.repository.AmountRepository;
import com.hhplus.io.common.support.domain.error.CoreException;
import com.hhplus.io.common.support.domain.error.ErrorType;
import org.springframework.stereotype.Component;

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
        if (amount == null) {
            throw new CoreException(ErrorType.USER_NOT_FOUND);
        }
        amount.saveAmount(updateAmount);
        return amount.getAmount();
    }

    public void payAmount(Long userId, int payment) {
        Amount amount = amountRepository.getAmountByUserWithLock(userId);
        if (amount.getAmount() - payment < 0) {
            throw new CoreException(ErrorType.FORBIDDEN);
        }
        amount.payAmount(payment);
    }
}
