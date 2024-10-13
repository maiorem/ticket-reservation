package com.hhplus.io.user.persistence;

import com.hhplus.io.common.exception.UserNotFoundException;
import com.hhplus.io.user.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository, JPAQueryFactory jpaQueryFactory) {
        this.userJpaRepository = userJpaRepository;
        this.jpaQueryFactory = jpaQueryFactory;
    }


    @Override
    public User getUserByUuid(String uuid) {
        Optional<User> optionalUser = userJpaRepository.findByUuid(uuid);
        return optionalUser.orElseThrow(() -> new UserNotFoundException("해당 사용자를 찾을 수 없습니다."));
    }
}
