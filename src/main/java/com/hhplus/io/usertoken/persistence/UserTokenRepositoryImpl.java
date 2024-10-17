package com.hhplus.io.usertoken.persistence;

import com.hhplus.io.usertoken.domain.entity.UserToken;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserTokenRepositoryImpl implements UserTokenRepository {

    private final UserTokenJpaRepository jpaRepository;

    public UserTokenRepositoryImpl(UserTokenJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public UserToken getToken(Long userId) {
        Optional<UserToken> optionalUserToken = jpaRepository.findByUserId(userId);
        return optionalUserToken.orElse(null);
    }

    @Override
    public UserToken generateToken(UserToken userToken) {
        return jpaRepository.save(userToken);
    }

    @Override
    public UserToken getUserByToken(String token) {
        Optional<UserToken> userToken = jpaRepository.findByToken(token);
        return userToken.orElse(null);
    }
}
