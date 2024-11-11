package com.hhplus.io.app.usertoken.domain.service;

import com.hhplus.io.common.support.domain.error.CoreException;
import com.hhplus.io.common.support.domain.error.ErrorType;
import com.hhplus.io.app.usertoken.domain.entity.User;
import com.hhplus.io.app.usertoken.domain.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(Long userId) {
        User user = userRepository.getUser(userId);
        if(user == null){
            throw new CoreException(ErrorType.USER_NOT_FOUND);
        }
        return user;
    }
}
