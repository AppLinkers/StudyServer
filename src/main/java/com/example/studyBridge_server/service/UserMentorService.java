package com.example.studyBridge_server.service;

import com.example.studyBridge_server.domaion.MentorProfile;
import com.example.studyBridge_server.domaion.User;
import com.example.studyBridge_server.domaion.type.Subject;
import com.example.studyBridge_server.dto.userMentor.ProfileRes;
import com.example.studyBridge_server.dto.userMentor.ProfileReq;
import com.example.studyBridge_server.dto.userMentor.ProfileTextReq;
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

        ProfileTextReq profileTextReq = profileReq.getProfileTextReq();


        // 업데이트 할 멘토 사용자 객체 불러오기
        User user = userRepository.findUserByLoginId(profileTextReq.getUserLoginId()).get();

        if (!user.getLocation().equals(profileTextReq.getLocation())) {
            user.setLocation(profileTextReq.getLocation());
            userRepository.save(user);
        }

        // 업데이트 할 멘토 프로필 불러오기
        MentorProfile mentorProfile = mentorProfileRepository.findByUser(user);
        mentorProfile.setInfo(profileTextReq.getInfo());
        mentorProfile.setNickName(profileTextReq.getNickName());
        mentorProfile.setSchool(profileTextReq.getSchool());
        mentorProfile.setSchoolImg(schoolImgUrl);
        mentorProfile.setCertificatesImg(java.util.Optional.of(certificatesImg));
//        mentorProfile.setSubject(Subject.valueOf(profileTextReq.getSubject()));
        mentorProfile.setSubject(profileTextReq.getSubject());
        mentorProfile.setExperience(profileTextReq.getExperience());
        mentorProfile.setCurriculum(profileTextReq.getCurriculum());
        mentorProfile.setAppeal(profileTextReq.getAppeal());

        MentorProfile result = mentorProfileRepository.save(mentorProfile);

        // 결과 생성
        return ProfileRes.builder()
                .userName(user.getName())
                .location(profileTextReq.getLocation())
                .info(result.getInfo())
                .nickName(result.getNickName())
                .school(result.getSchool())
                .schoolImg(result.getSchoolImg())
                .subject(result.getSubject().toString())
                .certificatesImg(result.getCertificatesImg().get())
                .curriculum(result.getCurriculum())
                .appeal(result.getAppeal())
                .build();

    }
}
