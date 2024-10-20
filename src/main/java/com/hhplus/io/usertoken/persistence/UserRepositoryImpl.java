package com.hhplus.io.usertoken.persistence;

import com.hhplus.io.usertoken.domain.entity.User;
import com.hhplus.io.usertoken.domain.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public User getUser(Long userId) {
        Optional<User> optionalUser = userJpaRepository.findByUserId(userId);
        return optionalUser.orElse(null);
    }

    @Override
    public User saveUser(User user) {
        return userJpaRepository.save(user);
    }

}
