package com.hhplus.io.usertoken.domain;

import org.springframework.stereotype.Repository;

@Repository
public interface UserTokenRepository {

    UserToken getToken(Long userId);

    UserToken generateToken(UserToken userToken);

    UserToken getUserByToken(String token);
}
