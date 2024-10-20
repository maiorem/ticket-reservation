package com.hhplus.io.usertoken.web;


import com.hhplus.io.common.response.ApiResponse;
import com.hhplus.io.usertoken.application.UserTokenUseCase;
import com.hhplus.io.usertoken.service.UserTokenService;
import com.hhplus.io.usertoken.web.request.TokenRequest;
import com.hhplus.io.usertoken.web.response.UserTokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserTokenService userTokenService;
    private final UserTokenUseCase userTokenUseCase;

    public UserController(UserTokenService userTokenService, UserTokenUseCase userTokenUseCase) {
        this.userTokenService = userTokenService;
        this.userTokenUseCase = userTokenUseCase;
    }


    @PostMapping("/token/{userId}")
    @Operation(summary = "유저 토큰 발급")
    public ApiResponse<?> generateToken(@PathVariable(name = "userId") @Schema(description = "유저 ID") Long userId){
        UserTokenResponse response = new UserTokenResponse("aaaa-111111", LocalDateTime.now().plusHours(1));
        return ApiResponse.success("data", response);
    }

    @PatchMapping("/wait/update")
    @Operation(summary = "대기 중인 모든 대기열 순서 업데이트")
    public ApiResponse<?> updateQueue(@RequestBody TokenRequest request){
        //토큰 잔여시간 검사 (만료 시 exception)
//        if(userTokenService.isExpireToken(request.token())) {
//            throw new TokenExpireException("토큰 만료시간을 초과하였습니다.");
//        }
        return ApiResponse.success("data", "대기열 업데이트가 완료되었습니다."+"::"+LocalDateTime.now());
    }

    @PostMapping("/wait/sequence")
    @Operation(summary = "유저 대기 순서 조회")
    public ApiResponse<?> getSequenceByQueue(@RequestBody TokenRequest request){
        //토큰 잔여시간 검사 (만료 시 exception)
//        if(userTokenService.isExpireToken(request.token())) {
//            throw new TokenExpireException("토큰 만료시간을 초과하였습니다.");
//        }
        Long response = 20L;
        return ApiResponse.success("data", response);
    }

}
