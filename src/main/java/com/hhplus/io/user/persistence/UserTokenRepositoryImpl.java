package com.hhplus.io.user.persistence;

import com.hhplus.io.user.domain.User;
import com.hhplus.io.user.domain.UserToken;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.Optional;

public class UserTokenRepositoryImpl implements UserTokenRepository {

    private final UserTokenJpaRepository jpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    public UserTokenRepositoryImpl(UserTokenJpaRepository jpaRepository, JPAQueryFactory jpaQueryFactory) {
        this.jpaRepository = jpaRepository;
        this.jpaQueryFactory = jpaQueryFactory;
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
}
