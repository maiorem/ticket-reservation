package com.hhplus.io.usertoken.domain;

import com.hhplus.io.common.exception.UserNotFoundException;
import com.hhplus.io.common.response.ErrorCode;
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
class UserInfoTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserInfo userInfo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userRepository = mock(UserRepository.class);
        userInfo = new UserInfo(userRepository);
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
        User testUser = userInfo.getUser(userId);

        //then
        assertEquals(userId, testUser.getUserId());
    }

    @Test
    @DisplayName("사용자 정보 없음")
    public void getUserNotFoundTest() {
        //given
        Long userId = 1L;

        //when
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userInfo.getUser(userId));

        //then
        assertEquals(ErrorCode.NOT_FOUND.getStatusCode(), exception.getStatusCode());
    }
}