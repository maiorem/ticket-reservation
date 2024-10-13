package com.hhplus.io.user.application;

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
        return userRepository.getUserByUuid(uuid);
    }

}
