package com.hhplus.io.user.persistence;

import com.hhplus.io.user.domain.User;
import com.hhplus.io.user.domain.UserToken;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTokenRepository {

    UserToken getToken(Long userId);

    UserToken generateToken(UserToken userToken);
}
