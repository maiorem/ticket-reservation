package com.hhplus.io.usertoken.domain;

import com.hhplus.io.support.domain.error.CoreException;
import com.hhplus.io.support.domain.error.ErrorType;
import com.hhplus.io.usertoken.domain.entity.User;
import com.hhplus.io.usertoken.domain.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserInfo {

    private final UserRepository userRepository;

    public UserInfo(UserRepository userRepository) {
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
