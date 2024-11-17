package com.hhplus.io.app.usertoken.application;


public record UserTokenInfo(Long userId, Long sequence, String token) {
    public static UserTokenInfo of(Long userId, Long sequence, String token) {
        return new UserTokenInfo(userId, sequence, token);
    }
}
