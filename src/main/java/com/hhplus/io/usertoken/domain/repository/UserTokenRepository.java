package com.hhplus.io.usertoken.domain.repository;

import com.hhplus.io.usertoken.domain.entity.User;
import com.hhplus.io.usertoken.domain.entity.UserToken;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTokenRepository {

    UserToken getToken(Long userId);

    UserToken generateToken(UserToken userToken);

    User getUserByToken(String token);

    UserToken getUserTokenByToken(String token);
}
