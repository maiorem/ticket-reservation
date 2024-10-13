package com.hhplus.io.user.application;

import com.hhplus.io.user.domain.User;
import com.hhplus.io.user.domain.UserToken;
import com.hhplus.io.user.persistence.UserTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserTokenService {

    private final UserTokenRepository userTokenRepository;

    public UserTokenService(UserTokenRepository userTokenRepository) {
        this.userTokenRepository = userTokenRepository;
    }

    public UserToken getUserToken(User user) {
        UserToken token = userTokenRepository.getToken(user.getUserId());
        if (token == null) {
            UserToken builder = UserToken.builder()
                    .userId(user.getUserId())
                    .uuid(user.getUuid())
                    .isActive(true)
                    .tokenExpire(LocalDateTime.now())
                    .build();
            token = userTokenRepository.generateToken(builder);
        }
        return token;
    }

}
