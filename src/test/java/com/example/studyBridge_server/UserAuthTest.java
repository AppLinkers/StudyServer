package com.example.studyBridge_server;

import com.example.studyBridge_server.dto.UserAuth.UserLoginReq;
import com.example.studyBridge_server.dto.UserAuth.UserLoginRes;
import com.example.studyBridge_server.dto.UserAuth.UserSignUpReq;
import com.example.studyBridge_server.dto.UserAuth.UserSignUpRes;
import com.example.studyBridge_server.repository.UserRepository;
import com.example.studyBridge_server.service.UserAuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

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
                .loginId("test_loginId")
                .loginPw("test_loginPw")
                .role("MENTOR")
                .phone("010-0000-0000")
                .gender("MALE")
                .location("test_location")
                .profileImg(new MockMultipartFile("name", "content".getBytes()))
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
                .loginId("test_loginId")
                .loginPw("test_loginPw")
                .role("MENTOR")
                .phone("010-0000-0000")
                .gender("MALE")
                .location("test_location")
                .profileImg(new MockMultipartFile("name", "content".getBytes()))
                .build();

        userAuthService.create(userSignUpReq);

        UserLoginReq userLoginReq = UserLoginReq.builder()
                .loginId("test_loginId")
                .loginPw("test_loginPw")
                .build();

        // when
        UserLoginRes userLoginRes = userAuthService.login(userLoginReq);

        // then
        assertEquals(userLoginReq.getLoginId(), userLoginRes.getLoginId());
    }
}
