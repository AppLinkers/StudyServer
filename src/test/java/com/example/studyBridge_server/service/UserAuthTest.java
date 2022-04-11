package com.example.studyBridge_server.service;

import com.example.studyBridge_server.domain.type.Gender;
import com.example.studyBridge_server.domain.type.Role;
import com.example.studyBridge_server.dto.userAuth.UserSignUpReq;
import com.example.studyBridge_server.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class UserAuthTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserAuthService userAuthService;

    @Test
    @DisplayName("User Create 테스트")
    void 회원가입() {
        // given
        UserSignUpReq request = giveMentee();


    }

    private UserSignUpReq giveMentee() {
        return UserSignUpReq.builder()
                .name("test_name")
                .loginId("test_loginId")
                .loginPw("test_loginPw")
                .gender("FEMALE")
                .role("MENTEE")
                .location("test_location")
                .phone("01012345678")
                .build();
    }
}
