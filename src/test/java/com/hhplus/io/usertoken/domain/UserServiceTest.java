package com.hhplus.io.usertoken.domain;

import com.hhplus.io.app.usertoken.domain.service.UserService;
import com.hhplus.io.common.support.domain.error.CoreException;
import com.hhplus.io.common.support.domain.error.ErrorType;
import com.hhplus.io.app.usertoken.domain.entity.User;
import com.hhplus.io.app.usertoken.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("사용자 정보 조회 성공")
    public void getUserTest() {
        //given
        Long userId = 1L;
        User user = User.builder()
                .userId(1L).username("홍세영")
                .build();
        when(userRepository.getUser(userId)).thenReturn(user);

        //when
        User testUser = userService.getUser(userId);

        //then
        assertEquals(userId, testUser.getUserId());
    }

    @Test
    @DisplayName("사용자 정보 없음")
    public void getUserNotFoundTest() {
        //given
        Long userId = 1L;

        //when
        CoreException exception = assertThrows(CoreException.class, () -> userService.getUser(userId));

        //then
        assertEquals(ErrorType.USER_NOT_FOUND, exception.getErrorType());
    }
}