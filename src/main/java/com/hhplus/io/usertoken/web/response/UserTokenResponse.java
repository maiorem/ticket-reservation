package com.hhplus.io.usertoken.web.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record UserTokenResponse(@Schema(description = "토큰") String token, @Schema(description = "토큰 만료 시간") LocalDateTime expires) {
}
