package com.example.studyBridge_server.domain;

import com.example.studyBridge_server.domain.type.Gender;
import com.example.studyBridge_server.domain.type.Role;
import com.example.studyBridge_server.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserTest {

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("User 생성 테스트")
    void userCreateTest() {
        // given
        User user = User.builder()
                .loginId("user1")
                .loginPw("test_pw")
                .gender(Gender.MALE)
                .name("test_name")
                .phone("01012345678")
                .location("test_location")
                .role(Role.MENTEE)
                .personalInfo(true)
                .build();

        List<User> users = new ArrayList<>();
        users.add(user);

        given(userRepository.findAll()).willReturn(users);

        // When
        List<User> findUsers = userRepository.findAll();

        // Then
        Assertions.assertEquals(1, findUsers.size());
        Assertions.assertEquals(user.getName(), findUsers.get(0).getName());
    }

}
