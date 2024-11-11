package com.hhplus.io.app.usertoken.application;


public record UserTokenCommand(Long userId, Long sequence, String token) {
    public static UserTokenCommand of(Long userId, Long sequence, String token) {
        return new UserTokenCommand(userId, sequence, token);
    }
}
