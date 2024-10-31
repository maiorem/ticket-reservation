package com.hhplus.io.amount.application;

import com.hhplus.io.amount.domain.entity.Amount;
import com.hhplus.io.amount.service.AmountService;
import com.hhplus.io.support.domain.error.CoreException;
import com.hhplus.io.support.domain.error.ErrorType;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AmountUseCase {

    private final AmountService amountService;
    public AmountUseCase(AmountService amountService) {
        this.amountService = amountService;
    }

    /**
     * 잔액 조회
     */
    public int getAmountByUser(Long userId){
        Amount amount = amountService.getAmountByUser(userId);
        if (amount == null){
            throw new CoreException(ErrorType.USER_NOT_FOUND);
        }
        return amount.getAmount();
    }


    /**
     * 잔액 충전
     */
    @Transactional
    public SaveAmountCommand saveAmount(Long userId, int updateAmount){
        if(updateAmount < 0) {
            throw new CoreException(ErrorType.FORBIDDEN);
        }
        int newAmount = amountService.updateAmount(userId, updateAmount);
        return SaveAmountCommand.of(userId, newAmount);
    }


}
