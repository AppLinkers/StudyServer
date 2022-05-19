package com.example.studyBridge_server.service;

import com.example.studyBridge_server.domain.MentorProfile;
import com.example.studyBridge_server.domain.type.Gender;
import com.example.studyBridge_server.domain.type.Role;
import com.example.studyBridge_server.domain.User;
import com.example.studyBridge_server.dto.userAuth.*;
import com.example.studyBridge_server.repository.MentorProfileRepository;
import com.example.studyBridge_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MentorProfileRepository mentorProfileRepository;
    private final S3Uploader s3Uploader;

    /**
     * create User entity
     */
    public UserSignUpRes create(UserSignUpReq userSignUpReq) {
        // 비밀번호 암호화
        String loginPw = passwordEncoder.encode(userSignUpReq.getLoginPw());

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

    /**
     * get user profile for userLoginId
     */
    public UserProfileRes profile(String userLoginId) {
        return userRepository.findProfileByStringId(userLoginId);
    }

    /**
     * update user profile
     */
    @Transactional
    public UserProfileRes updateProfile(UserProfileUpdateReq userprofileUpdateReq) throws IOException {
        User target = userRepository.findUserByLoginId(userprofileUpdateReq.getLoginId()).orElseThrow(() -> new RuntimeException("wrong_loginId"));

        if (userprofileUpdateReq.getProfileImg() == null) {
            // 이미지 업로드
            String imgUrl = s3Uploader.upload(userprofileUpdateReq.getProfileImg().get(), "user/profile");

            target.setProfileImg(imgUrl);
        }

        target.setName(userprofileUpdateReq.getName());
        target.setPhone(userprofileUpdateReq.getPhone());
        target.setLocation(userprofileUpdateReq.getLocation());
        target.setGender(userprofileUpdateReq.getGender());

        User savedUser = userRepository.save(target);

        UserProfileRes result = userToUserProfileRes(savedUser);

        return result;
    }

    /**
     * update user profile image
     */
    @Transactional
    public UserProfileRes updateProfileImg(String userLoginId, MultipartFile imgFile) throws IOException {
        String imgUrl = s3Uploader.upload(imgFile, "user/profile");

        User target = userRepository.findUserByLoginId(userLoginId).orElseThrow(() -> new RuntimeException("wrong_loginId"));

        target.setProfileImg(imgUrl);

        User savedUser = userRepository.save(target);

        UserProfileRes result = userToUserProfileRes(savedUser);

        return result;
    }

    /**
     * User -> UserProfileRes
     */
    public UserProfileRes userToUserProfileRes(User user) {
        return UserProfileRes.builder()
                .loginId(user.getLoginId())
                .name(user.getName())
                .phone(user.getPhone())
                .profileImg(user.getProfileImg())
                .location(user.getLocation())
                .gender(user.getGender())
                .role(user.getRole())
                .build();
    }
}
