package com.hhplus.io.amount.application;

import com.hhplus.io.amount.domain.entity.Amount;
import com.hhplus.io.amount.domain.service.AmountService;
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
        return amount.getAmount();
    }


    /**
     * 잔액 충전
     */
    public SaveAmountCommand saveAmount(Long userId, int updateAmount){
        int newAmount = amountService.updateAmount(userId, updateAmount);
        return SaveAmountCommand.of(userId, newAmount);

    }


}
