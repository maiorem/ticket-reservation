package com.hhplus.io.amount.web;

import com.hhplus.io.common.exception.TokenExpireException;
import com.hhplus.io.common.response.ApiResponse;
import com.hhplus.io.amount.application.AmountUseCase;
import com.hhplus.io.amount.web.request.SaveAmountRequest;
import com.hhplus.io.amount.web.response.SaveAmountResponse;
import com.hhplus.io.usertoken.domain.service.UserTokenService;
import com.hhplus.io.usertoken.web.request.TokenRequest;
import com.hhplus.io.usertoken.web.response.RetrieveChargeAmountResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AmountController {

    private final AmountUseCase amountUseCase;
    private final UserTokenService userTokenService;

    public AmountController(AmountUseCase amountUseCase, UserTokenService userTokenService) {
        this.amountUseCase = amountUseCase;
        this.userTokenService = userTokenService;
    }

    @PostMapping("/balance")
    @Operation(summary = "잔액 조회")
    public ApiResponse<?> getBalance(@RequestBody TokenRequest request){
        //토큰 잔여시간 검사 (만료 시 exception)
//        if(userTokenService.isExpireToken(request.token())) {
//            throw new TokenExpireException("토큰 만료시간을 초과하였습니다.");
//        }
        RetrieveChargeAmountResponse response = new RetrieveChargeAmountResponse(10000);
        return ApiResponse.success("data", response);
    }

    @PatchMapping("/balance/save")
    @Operation(summary = "잔액 충전")
    public ApiResponse<?> saveBalance(@RequestBody SaveAmountRequest request){
        //토큰 잔여시간 검사 (만료 시 exception)
//        if(userTokenService.isExpireToken(request.token())) {
//            throw new TokenExpireException("토큰 만료시간을 초과하였습니다.");
//        }
        SaveAmountResponse response = new SaveAmountResponse(30000);
        return ApiResponse.success("data", response);
    }

}
