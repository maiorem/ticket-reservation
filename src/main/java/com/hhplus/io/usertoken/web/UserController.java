package com.hhplus.io.usertoken.web;


import com.hhplus.io.common.response.ApiResponse;
import com.hhplus.io.usertoken.web.response.UserTokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @PostMapping("/token/{userId}")
    @Operation(summary = "유저 토큰 발급")
    public ApiResponse<?> generateToken(@PathVariable(name = "userId") @Schema(description = "유저 ID") Long userId){
        UserTokenResponse response = new UserTokenResponse("aaaa-111111", LocalDateTime.now().plusHours(1));
        return ApiResponse.success("data", response);
    }

}
