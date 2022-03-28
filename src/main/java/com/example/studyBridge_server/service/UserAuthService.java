package com.example.studyBridge_server.service;

import com.example.studyBridge_server.domaion.MentorProfile;
import com.example.studyBridge_server.domaion.type.Gender;
import com.example.studyBridge_server.domaion.type.Role;
import com.example.studyBridge_server.domaion.User;
import com.example.studyBridge_server.dto.userAuth.UserLoginReq;
import com.example.studyBridge_server.dto.userAuth.UserLoginRes;
import com.example.studyBridge_server.dto.userAuth.UserSignUpReq;
import com.example.studyBridge_server.dto.userAuth.UserSignUpRes;
import com.example.studyBridge_server.repository.MentorProfileRepository;
import com.example.studyBridge_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MentorProfileRepository mentorProfileRepository;

    public UserSignUpRes create(UserSignUpReq userSignUpReq) {
        // 비밀번호 암호화
        String loginPw = passwordEncoder.encode(userSignUpReq.getLoginPw());

        // profile img 처리
        //String ImgUrl = s3Uploader.upload(userSignUpReq.getProfileImg(), "user/profile");
        String ImgUrl = "https://study-bridge.s3.us-east-2.amazonaws.com/user/profile/basic.png";

        User user = User.builder()
                .name(userSignUpReq.getName())
                .loginId(userSignUpReq.getLoginId())
                .loginPw(loginPw)
                .gender(Gender.valueOf(userSignUpReq.getGender()))
                .role(Role.valueOf(userSignUpReq.getRole()))
                .phone(userSignUpReq.getPhone())
                .location(userSignUpReq.getLocation())
                .profileImg(ImgUrl)
                .build();

        User createdUser = userRepository.save(user);

        // 사용자가 멘토 일 때, 초기 프로필 작성
        if (createdUser.getRole().equals(Role.MENTOR)) {
            MentorProfile mentorProfile = MentorProfile.initialize()
                    .user(createdUser)
                    .build();
            mentorProfileRepository.save(mentorProfile);
        }

        // 결과 반환
        UserSignUpRes userSignUpRes = UserSignUpRes.builder()
                .name(createdUser.getName())
                .loginId(createdUser.getLoginId())
                .build();

        return userSignUpRes;
    }

    public UserLoginRes login(UserLoginReq userLoginReq) {
        User target = userRepository.findUserByLoginId(userLoginReq.getLoginId()).orElseThrow(() -> new RuntimeException("wrong_loginId"));
        UserLoginRes userLoginRes;
        if (passwordEncoder.matches(userLoginReq.getLoginPw(), target.getLoginPw())) {
            userLoginRes = UserLoginRes.builder()
                    .id(target.getId())
                    .loginId(target.getLoginId())
                    .name(target.getName())
                    .build();

        }
        // 비밀번호 일치하지 않을 시
        else {
            throw new RuntimeException("wrong_loginPw");
        }

        return userLoginRes;
    }

    public boolean IdValidChk(String loginId) {
        return  userRepository.findUserByLoginId(loginId).isEmpty();
    }

    public boolean isMentee(String loginId) {
        return userRepository.findRoleByLoginId(loginId).equals(Role.MENTEE);
    }

    public String getName(Long userId) {
        return userRepository.findNameById(userId);
    }


}
