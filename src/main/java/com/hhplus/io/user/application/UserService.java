package com.hhplus.io.user.application;

import com.hhplus.io.common.exception.UserNotFoundException;
import com.hhplus.io.user.domain.User;
import com.hhplus.io.user.persistence.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByUuid(String uuid){
        User user = userRepository.getUserByUuid(uuid);
        if(user == null){
            throw new UserNotFoundException("해당 사용자를 찾을 수 없습니다.");
        }
        return user;
    }

}
