package com.hhplus.io.usertoken.domain;

import com.hhplus.io.common.exception.UserNotFoundException;
import com.hhplus.io.usertoken.domain.entity.User;
import com.hhplus.io.usertoken.domain.entity.UserToken;
import com.hhplus.io.usertoken.persistence.UserRepository;
import com.hhplus.io.usertoken.persistence.UserTokenRepository;
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
            throw new UserNotFoundException("해당 사용자를 찾을 수 없습니다.");
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

    public UserToken getUserByToken(String token) {
        return userTokenRepository.getUserByToken(token);
    }

    public boolean isExpireToken(String token) {
        UserToken resultToken = userTokenRepository.getUserByToken(token);
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        return now.isAfter(resultToken.getTokenExpire());
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
}
