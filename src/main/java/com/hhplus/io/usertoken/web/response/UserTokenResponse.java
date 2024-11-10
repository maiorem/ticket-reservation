package com.hhplus.io.usertoken.web.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserTokenResponse(@Schema(description = "유저 ID") Long userId,
                                @Schema(description = "토큰") String token,
                                @Schema(description = "대기순서") Long sequence) {
    public static UserTokenResponse of(Long userId, String token, Long sequence) {
        return new UserTokenResponse(userId, token, sequence);
    }
}
