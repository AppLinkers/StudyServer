package com.example.studyBridge_server.service;

import com.example.studyBridge_server.domaion.MentorProfile;
import com.example.studyBridge_server.domaion.User;
import com.example.studyBridge_server.domaion.type.Subject;
import com.example.studyBridge_server.dto.userMentor.ProfileRes;
import com.example.studyBridge_server.dto.userMentor.ProfileReq;
import com.example.studyBridge_server.repository.MentorProfileRepository;
import com.example.studyBridge_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserMentorService {

    private final MentorProfileRepository mentorProfileRepository;
    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;

    public ProfileRes profile(ProfileReq profileReq) throws IOException {

        // img 업로드
        String schoolImgUrl = s3Uploader.upload(profileReq.getSchoolImg(), "mentor/profile");
        List<String> certificatesImg = new ArrayList<>();
        if (profileReq.getCertificatesImg().size() > 0) {
            profileReq.getCertificatesImg().forEach(image -> {
                try {
                    String certificateImgUrl = s3Uploader.upload(image, "mentor/profile");
                    certificatesImg.add(certificateImgUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }


        // 업데이트 할 멘토 사용자 객체 불러오기
        User user = userRepository.findUserByLoginId(profileReq.getUserLoginId()).get();

        if (!user.getLocation().equals(profileReq.getLocation())) {
            user.setLocation(profileReq.getLocation());
            userRepository.save(user);
        }

        // 업데이트 할 멘토 프로필 불러오기
        MentorProfile mentorProfile = mentorProfileRepository.findByUser(user);
        mentorProfile.setInfo(profileReq.getInfo());
        mentorProfile.setNickName(profileReq.getNickName());
        mentorProfile.setSchool(profileReq.getSchool());
        mentorProfile.setSchoolImg(schoolImgUrl);
        mentorProfile.setCertificatesImg(certificatesImg);
        mentorProfile.setSubject(Subject.valueOf(profileReq.getSubject()));
        mentorProfile.setExperience(profileReq.getExperience());
        mentorProfile.setCurriculum(profileReq.getCurriculum());
        mentorProfile.setAppeal(profileReq.getAppeal());

        MentorProfile result = mentorProfileRepository.save(mentorProfile);

        // 결과 생성
        return ProfileRes.builder()
                .userName(user.getName())
                .location(profileReq.getLocation())
                .info(result.getInfo())
                .nickName(result.getNickName())
                .school(result.getSchool())
                .schoolImg(result.getSchoolImg())
                .subject(result.getSubject().toString())
                .certificatesImg(result.getCertificatesImg())
                .curriculum(result.getCurriculum())
                .appeal(result.getAppeal())
                .build();

    }
}
