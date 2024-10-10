package com.hhplus.io.user.web;


import com.hhplus.io.common.response.ApiResponse;
import com.hhplus.io.user.web.request.SaveAmountRequest;
import com.hhplus.io.user.web.response.RetrieveChargeAmountResponse;
import com.hhplus.io.user.web.response.SaveAmountResponse;
import com.hhplus.io.user.web.response.UserTokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @PostMapping("/token")
    @Operation(summary = "유저 토큰 발급")
    public ApiResponse<?> generateToken(@RequestParam @Schema(description = "암호화 된 user 식별 uuid") String uuid){
        UserTokenResponse response = new UserTokenResponse("aaaa-111111", "2024-10-12 12:00:00");
        return ApiResponse.success("data", response);
    }

    @PostMapping("/balance")
    @Operation(summary = "잔액 조회")
    public ApiResponse<?> getBalance(@RequestParam @Schema(description = "유저 토큰") String token){
        RetrieveChargeAmountResponse response = new RetrieveChargeAmountResponse(10000);
        return ApiResponse.success("data", response);
    }

    @PostMapping("/balance/save")
    @Operation(summary = "잔액 충전")
    public ApiResponse<?> saveBalance(@RequestBody SaveAmountRequest request){
        SaveAmountResponse response = new SaveAmountResponse(30000);
        return ApiResponse.success("data", response);
    }

}
