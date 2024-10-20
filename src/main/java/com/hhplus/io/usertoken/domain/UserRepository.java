package com.hhplus.io.usertoken.domain;


import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    User getUser(Long userId);
    User saveUser(User user);
}
