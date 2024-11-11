package com.hhplus.io.app.amount.web;

import com.hhplus.io.app.amount.application.SaveAmountCommand;
import com.hhplus.io.app.amount.web.response.RetrieveChargeAmountResponse;
import com.hhplus.io.common.interfaces.ApiResponse;
import com.hhplus.io.app.amount.application.AmountUseCase;
import com.hhplus.io.app.amount.web.request.SaveAmountRequest;
import com.hhplus.io.app.amount.web.response.SaveAmountResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/balance")
public class AmountController {

    private final AmountUseCase amountUseCase;

    public AmountController(AmountUseCase amountUseCase) {
        this.amountUseCase = amountUseCase;
    }

    @PostMapping("/{userId}")
    @Operation(summary = "잔액 조회")
    public ApiResponse<?> getBalance(@PathVariable(name = "userId") @Schema(description = "유저 ID") Long userId) {
        int remainAmount = amountUseCase.getAmountByUser(userId);
        RetrieveChargeAmountResponse response = RetrieveChargeAmountResponse.of(userId, remainAmount);
        return ApiResponse.success("data", response);
    }

    @PatchMapping("/save")
    @Operation(summary = "잔액 충전")
    public ApiResponse<?> saveBalance(@RequestBody SaveAmountRequest request){
        SaveAmountCommand command = amountUseCase.saveAmount(request.userId(), request.saveAmount());
        SaveAmountResponse response = SaveAmountResponse.of(command.userId(), command.amount());
        return ApiResponse.success("data", response);
    }

}
