package com.hhplus.io.support.utils;

import com.hhplus.io.usertoken.domain.entity.User;
import com.hhplus.io.usertoken.service.UserTokenService;
import org.springframework.stereotype.Component;

@Component
public class TokenValidator {

    private final UserTokenService userTokenService;

    public TokenValidator(UserTokenService userTokenService) {
        this.userTokenService = userTokenService;
    }

    public boolean validate(String token) {
        User user = userTokenService.getUserByToken(token);
        return userTokenService.validate(user, token);
    }

    public boolean isExpired(String token) {
        return userTokenService.isExpireToken(token);
    }
}
