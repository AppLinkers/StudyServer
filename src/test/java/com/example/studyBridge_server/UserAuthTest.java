package com.example.studyBridge_server;

import com.example.studyBridge_server.dto.userAuth.UserLoginReq;
import com.example.studyBridge_server.dto.userAuth.UserLoginRes;
import com.example.studyBridge_server.dto.userAuth.UserSignUpReq;
import com.example.studyBridge_server.dto.userAuth.UserSignUpRes;
import com.example.studyBridge_server.repository.UserRepository;
import com.example.studyBridge_server.service.UserAuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


import javax.transaction.Transactional;

@SpringBootTest
public class UserAuthTest {

    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    void create() {
        // given
        UserSignUpReq userSignUpReq = UserSignUpReq.builder()
                .name("test_name")
                .loginId("test_loginId2")
                .loginPw("test_loginPw2")
                .role("MENTOR")
                .phone("010-0000-0000")
                .gender("MALE")
                .location("test_location")
                .personalInfo(true)
                .build();

        // when
        UserSignUpRes userSignUpRes = userAuthService.create(userSignUpReq);

        // then
        assertEquals(userSignUpReq.getName(), userRepository.findUserByLoginId(userSignUpRes.getLoginId()).get().getName() );
    }

    @Test
    @Transactional
    void login() {
        // given
        UserSignUpReq userSignUpReq = UserSignUpReq.builder()
                .name("test_name")
                .loginId("test_loginId2")
                .loginPw("test_loginPw2")
                .role("MENTOR")
                .phone("010-0000-0000")
                .gender("MALE")
                .location("test_location")
                .personalInfo(true)
                .build();

        userAuthService.create(userSignUpReq);

        UserLoginReq userLoginReq = UserLoginReq.builder()
                .loginId("test_loginId2")
                .loginPw("test_loginPw2")
                .build();

        // when
        UserLoginRes userLoginRes = userAuthService.login(userLoginReq);

        // then
        assertEquals(userLoginReq.getLoginId(), userLoginRes.getLoginId());
    }
}
