package com.hhplus.io.concert.web.request;


import io.swagger.v3.oas.annotations.media.Schema;

public record AvailableDateRequest(@Schema(description = "유저 토큰") String token, @Schema(description = "콘서트 ID") Long concertId) {

}
