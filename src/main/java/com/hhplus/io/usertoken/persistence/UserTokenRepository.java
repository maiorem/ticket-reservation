package com.hhplus.io.usertoken.persistence;

import com.hhplus.io.usertoken.domain.entity.UserToken;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTokenRepository {

    UserToken getToken(Long userId);

    UserToken generateToken(UserToken userToken);

    UserToken getUserByToken(String token);
}
