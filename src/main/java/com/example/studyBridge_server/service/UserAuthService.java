package com.example.studyBridge_server.service;

import com.example.studyBridge_server.domaion.type.Gender;
import com.example.studyBridge_server.domaion.type.Role;
import com.example.studyBridge_server.domaion.User;
import com.example.studyBridge_server.dto.userAuth.UserLoginReq;
import com.example.studyBridge_server.dto.userAuth.UserLoginRes;
import com.example.studyBridge_server.dto.userAuth.UserSignUpReq;
import com.example.studyBridge_server.dto.userAuth.UserSignUpRes;
import com.example.studyBridge_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Uploader s3Uploader;

    public UserSignUpRes create(UserSignUpReq userSignUpReq) {
        System.out.println(userSignUpReq.getName());
        User user = new User();

        user.setName(userSignUpReq.getName());
        user.setLoginId(userSignUpReq.getLoginId());

        // 비밀번호 암호화
        user.setLoginPw(passwordEncoder.encode(userSignUpReq.getLoginPw()));

        user.setGender(Gender.valueOf(userSignUpReq.getGender()));
        user.setRole(Role.valueOf(userSignUpReq.getRole()));
        user.setPhone(userSignUpReq.getPhone());
        user.setLocation(userSignUpReq.getLocation());

        // profile img 처리
//        String ImgUrl = s3Uploader.upload(userSignUpReq.getProfileImg(), "user/profile");
        String ImgUrl = "https://study-bridge.s3.us-east-2.amazonaws.com/user/profile/basic.png";
        user.setProfileImg(ImgUrl);

        User createdUser = userRepository.save(user);

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
        if (userRepository.findUserByLoginId(loginId).isPresent()) {
            return false;
        }
        else {
            return true;
        }
    }

}
