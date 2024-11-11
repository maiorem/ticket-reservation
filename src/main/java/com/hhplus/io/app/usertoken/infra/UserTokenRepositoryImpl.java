package com.hhplus.io.app.usertoken.infra;

import com.hhplus.io.app.usertoken.domain.entity.User;
import com.hhplus.io.app.usertoken.domain.entity.UserToken;
import com.hhplus.io.app.usertoken.domain.repository.UserTokenRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.hhplus.io.usertoken.domain.entity.QUser.user;
import static com.hhplus.io.usertoken.domain.entity.QUserToken.userToken;

@Repository
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

    @Override
    public User getUserByToken(String token) {
        return jpaQueryFactory
                .selectFrom(user)
                .join(userToken)
                .on(user.userId.eq(userToken.userId))
                .where(userToken.token.eq(token))
                .fetchOne();
    }

    @Override
    public UserToken getUserTokenByToken(String token) {
        Optional<UserToken> optional = jpaRepository.findByToken(token);
        return optional.orElse(null);
    }
}
