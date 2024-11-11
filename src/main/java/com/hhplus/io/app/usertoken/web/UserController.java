package com.hhplus.io.app.usertoken.web;


import com.hhplus.io.common.interfaces.ApiResponse;
import com.hhplus.io.app.usertoken.application.UserTokenCommand;
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
    public ApiResponse<?> generateToken(@PathVariable(name = "userId") @Schema(description = "유저 ID") Long userId){
        UserTokenCommand userTokenCommand = userTokenUseCase.issueUserToken(userId);
        UserTokenResponse response = UserTokenResponse.of(
                userTokenCommand.userId(),
                userTokenCommand.token(),
                userTokenCommand.sequence()
        );
        return ApiResponse.success("data", response);
    }

    @PostMapping("/api/sequence/{userId}")
    @Operation(summary = "유저 대기 순서 조회")
    public ApiResponse<?> getSequenceByQueue(@PathVariable(name = "userId") @Schema(description = "유저 ID") Long userId){
        Long response = userTokenUseCase.getSequence(userId);
        return ApiResponse.success("data", response);
    }

}
