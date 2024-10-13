package com.hhplus.io.user.application;

import com.hhplus.io.common.exception.UserNotFoundException;
import com.hhplus.io.user.domain.User;
import com.hhplus.io.user.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        String findUuid = "aaaa";
        User user = User.builder()
                .userId(1L).username("홍세영").uuid(findUuid).chargeAmount(100000)
                .build();
        when(userRepository.getUserByUuid(findUuid)).thenReturn(user);

        //when
        User testUser = userService.getUserByUuid(findUuid);

        //then
        assertEquals(findUuid, testUser.getUuid());
    }

    @Test
    @DisplayName("사용자 정보 없음")
    public void getUserNotFoundTest() {
        //given
        String findUuid1 = "aaaa";
        String findUuid2 = "bbbb";
        User user1 = User.builder()
                .userId(1L).username("홍세영").uuid("aaaa").chargeAmount(100000).build();
        when(userRepository.getUserByUuid(findUuid1)).thenReturn(user1);

        //when
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.getUserByUuid(findUuid2));

        //then
        assertEquals("해당 사용자를 찾을 수 없습니다.", exception.getStatusMessage());
    }


}