package com.hhplus.io.app.usertoken.domain.service;

import com.hhplus.io.app.usertoken.domain.UserInfo;
import com.hhplus.io.app.usertoken.domain.entity.User;
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
