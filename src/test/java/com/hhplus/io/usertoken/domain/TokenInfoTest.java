package com.hhplus.io.usertoken.domain;

import com.hhplus.io.app.usertoken.domain.TokenInfo;
import com.hhplus.io.app.usertoken.domain.UserInfo;
import com.hhplus.io.common.support.domain.error.CoreException;
import com.hhplus.io.common.support.domain.error.ErrorType;
import com.hhplus.io.app.usertoken.domain.entity.User;
import com.hhplus.io.app.usertoken.domain.entity.UserToken;
import com.hhplus.io.app.usertoken.domain.repository.UserRepository;
import com.hhplus.io.app.usertoken.domain.repository.UserTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenInfoTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserTokenRepository userTokenRepository;

    @InjectMocks
    private TokenInfo tokenInfo;

    @InjectMocks
    private UserInfo userInfo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userTokenRepository = mock(UserTokenRepository.class);
        userRepository = mock(UserRepository.class);
        tokenInfo = new TokenInfo(userTokenRepository, userRepository);
        userInfo = new UserInfo(userRepository);
    }

    @Test
    @DisplayName("유저 정보로 토큰 조회")
    void getUserToken() {
        //given
        Long userId = 1L;
        UserToken givenToken = UserToken.builder().tokenId(1L).userId(userId).token("aaaa").isActive(true).tokenExpire(LocalDateTime.now()).build();
        when(userTokenRepository.getToken(userId)).thenReturn(givenToken);

        //when
        UserToken testToken = tokenInfo.getUserToken(userId);

        //then
        assertEquals(givenToken.getToken(), testToken.getToken());

    }

    @Test
    @DisplayName("존재하지 않는 유저 아이디로 토큰 조회시 exception 반환")
    void 존재하지_않는_유저아이디로_토큰_조회_시_exception() {
        //given
        Long userId = 1L;

        //when
        CoreException exception = assertThrows(CoreException.class, () -> tokenInfo.getUserToken(userId));

        //then
        assertEquals(ErrorType.USER_NOT_FOUND, exception.getErrorType());

    }

    @Test
    @DisplayName("토큰 생성")
    void generator() {
        //given
        Long userId = 1L;
        User user = User.builder()
                .userId(1L).username("홍세영")
                .build();
        when(userRepository.getUser(userId)).thenReturn(user);

        //when
        UserToken generateToken = tokenInfo.generator(user);

        //then
        assertEquals(userId, generateToken.getUserId());
    }
}


