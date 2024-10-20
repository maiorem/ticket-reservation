package com.hhplus.io.usertoken.service;

import com.hhplus.io.usertoken.domain.TokenInfo;
import com.hhplus.io.usertoken.domain.entity.User;
import com.hhplus.io.usertoken.domain.entity.UserToken;
import org.springframework.stereotype.Service;

@Service
public class UserTokenService {

    private final TokenInfo tokenInfo;

    public UserTokenService(TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

    public UserToken createUserToken(User user) {
        return tokenInfo.generator(user);
    }

    public UserToken getUserByToken(String token) {
        return tokenInfo.getUserByToken(token);
    }

    public boolean validate(User user, String token) {
        return tokenInfo.validate(user, token);
    }

    public boolean isExpireToken(String token) {
        return tokenInfo.isExpireToken(token);

    }

    public void expireToken(UserToken userToken) {
        tokenInfo.expireToken(userToken);
    }
}
