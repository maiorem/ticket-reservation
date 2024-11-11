package com.hhplus.io.app.usertoken.domain;

import com.hhplus.io.common.support.domain.error.CoreException;
import com.hhplus.io.common.support.domain.error.ErrorType;
import com.hhplus.io.app.usertoken.domain.entity.User;
import com.hhplus.io.app.usertoken.domain.entity.UserToken;
import com.hhplus.io.app.usertoken.domain.repository.UserRepository;
import com.hhplus.io.app.usertoken.domain.repository.UserTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;

@Slf4j
@Component
public class TokenInfo {

    private final UserTokenRepository userTokenRepository;
    private final UserRepository userRepository;

    public TokenInfo(UserTokenRepository userTokenRepository, UserRepository userRepository) {
        this.userTokenRepository = userTokenRepository;
        this.userRepository = userRepository;
    }

    public UserToken getUserToken(Long userId) {
        User user = userRepository.getUser(userId);
        if (user == null) {
            throw new CoreException(ErrorType.USER_NOT_FOUND);
        }
        return userTokenRepository.getToken(user.getUserId());
    }

    public boolean validate(User user, String token) {
        boolean result = false;
        UserToken resultToken = userTokenRepository.getToken(user.getUserId());
        if (token.equals(resultToken.getToken())) {
            result = true;
        }
        return result;
    }

    public User getUserByToken(String token) {
        return userTokenRepository.getUserByToken(token);
    }

    public boolean isExpireToken(String token) {
        UserToken resultToken = userTokenRepository.getUserTokenByToken(token);
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        if (now.isAfter(resultToken.getTokenExpire())) return true;
        if (!resultToken.isActive()) return true;
        return false;
    }

    public UserToken generator(User user) {
        String token = Base64.getEncoder().encodeToString(String.valueOf(user.getUserId()).getBytes())+"_"+ System.currentTimeMillis();
        log.info("token :::::: {}", token);
        UserToken builder = UserToken.builder()
                .userId(user.getUserId())
                .token(token)
                .isActive(true)
                .tokenExpire(LocalDateTime.now().plusHours(1))
                .build();
        return userTokenRepository.generateToken(builder);
    }

    public void expireToken(UserToken userToken) {
        userToken.deleteToken();
    }

    public UserToken getUserTokenByToken(String token) {
        return userTokenRepository.getUserTokenByToken(token);
    }
}
