package com.hhplus.io.usertoken.persistence;


import com.hhplus.io.usertoken.domain.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    User getUser(Long userId);
    User saveUser(User user);
}
