package com.hhplus.io.usertoken.application;

import java.time.LocalDateTime;

public record UserTokenCommand(Long userId, Long sequence, String token, LocalDateTime expiresAt) {
    public static UserTokenCommand of(Long userId, Long sequence, String token, LocalDateTime tokenExpire) {
        return new UserTokenCommand(userId, sequence, token, tokenExpire);
    }
}
