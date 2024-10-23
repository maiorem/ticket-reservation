package com.hhplus.io.usertoken.web.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record UserTokenResponse(@Schema(description = "유저 ID") Long userId,
                                @Schema(description = "토큰") String token,
                                @Schema(description = "토큰 만료 시간")  LocalDateTime expires,
                                @Schema(description = "대기순서") Long sequence) {
    public static UserTokenResponse of(Long userId, String token, LocalDateTime expires, Long sequence) {
        return new UserTokenResponse(userId, token, expires, sequence);
    }
}
