package com.hhplus.io.usertoken.service;

import com.hhplus.io.usertoken.domain.UserInfo;
import com.hhplus.io.usertoken.domain.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserInfo userInfo;

    public UserService(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public User getUser(Long userId){
        return userInfo.getUser(userId);
    }

}
