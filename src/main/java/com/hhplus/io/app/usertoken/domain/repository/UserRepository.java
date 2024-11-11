package com.hhplus.io.app.usertoken.domain.repository;


import com.hhplus.io.app.usertoken.domain.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    User getUser(Long userId);
    User saveUser(User user);
}
