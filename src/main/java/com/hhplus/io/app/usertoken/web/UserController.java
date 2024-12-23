package com.hhplus.io.app.usertoken.web;


import com.hhplus.io.common.interfaces.ApiResponse;
import com.hhplus.io.app.usertoken.application.UserTokenInfo;
import com.hhplus.io.app.usertoken.application.UserTokenUseCase;
import com.hhplus.io.app.usertoken.web.response.UserTokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserTokenUseCase userTokenUseCase;

    public UserController(UserTokenUseCase userTokenUseCase) {
        this.userTokenUseCase = userTokenUseCase;
    }

    @PostMapping("/issue/{userId}")
    @Operation(summary = "유저 토큰 발급")
    public ApiResponse<?> generateToken(@PathVariable(name = "userId") @Schema(description = "유저 ID") Long userId,
                                        @RequestHeader(required = false, name = "token") String token){
        UserTokenInfo userTokenInfo = userTokenUseCase.issueUserToken(userId, token);
        UserTokenResponse response = UserTokenResponse.of(
                userTokenInfo.userId(),
                userTokenInfo.token(),
                userTokenInfo.sequence()
        );
        return ApiResponse.success("data", response);
    }

    @PostMapping("/api/sequence")
    @Operation(summary = "유저 대기 순서 조회")
    public ApiResponse<?> getSequenceByQueue(@RequestHeader(required = false, name = "token") String token){
        Long response = userTokenUseCase.getSequence(token);
        return ApiResponse.success("data", response);
    }

}
