package com.hhplus.io.usertoken.domain;

import com.hhplus.io.common.exception.error.UserNotFoundException;
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
            throw new UserNotFoundException("해당 사용자를 찾을 수 없습니다.");
        }
        return user;
    }
}
