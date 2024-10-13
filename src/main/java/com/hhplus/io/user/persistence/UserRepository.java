package com.hhplus.io.user.persistence;


import com.hhplus.io.user.domain.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {

    User getUserByUuid(String uuid);

    User generateUser(User user);
}
