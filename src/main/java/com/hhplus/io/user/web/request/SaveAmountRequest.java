package com.hhplus.io.user.web.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record SaveAmountRequest(@Schema(description = "유저 토큰") String token, @Schema(description = "충전할 금액") int saveAmount) {
}
