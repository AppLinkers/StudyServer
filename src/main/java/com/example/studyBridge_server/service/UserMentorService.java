package com.example.studyBridge_server.service;

import com.example.studyBridge_server.domaion.CertificateImg;
import com.example.studyBridge_server.domaion.MentorProfile;
import com.example.studyBridge_server.domaion.User;
import com.example.studyBridge_server.dto.userMentor.ProfileRes;
import com.example.studyBridge_server.dto.userMentor.ProfileReq;
import com.example.studyBridge_server.dto.userMentor.ProfileTextReq;
import com.example.studyBridge_server.repository.CertificateImgRepository;
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
    private final CertificateImgRepository certificateImgRepository;
    private final S3Uploader s3Uploader;

    List<CertificateImg> certificateImgs = new ArrayList<>();

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
//        mentorProfile.setCertificatesImg(java.util.Optional.of(certificatesImg));
        mentorProfile.setSubject(profileTextReq.getSubject());
        mentorProfile.setExperience(profileTextReq.getExperience());
        mentorProfile.setCurriculum(profileTextReq.getCurriculum());
        mentorProfile.setAppeal(profileTextReq.getAppeal());

        MentorProfile result = mentorProfileRepository.save(mentorProfile);

        // 기존 이미지는 어떻게 할것인지?
        if (!certificatesImg.isEmpty()) {
            List<CertificateImg> saveCertificatesImg = new ArrayList<>();
            certificatesImg.forEach(certificateImgUrl -> {
                CertificateImg certificateImg = new CertificateImg();

                certificateImg.setMentorProfile(result);
                certificateImg.setImgUrl(certificateImgUrl);

                saveCertificatesImg.add(certificateImg);
            });

             certificateImgs = certificateImgRepository.saveAll(saveCertificatesImg);
        }

        // 결과 생성
        return ProfileRes.builder()
                .userName(user.getName())
                .location(profileTextReq.getLocation())
                .info(result.getInfo())
                .nickName(result.getNickName())
                .school(result.getSchool())
                .schoolImg(result.getSchoolImg())
                .subject(result.getSubject().toString())
                .certificatesImg(certificatesImg)
                .experience(result.getExperience())
                .curriculum(result.getCurriculum())
                .appeal(result.getAppeal())
                .build();

    }

    public ProfileRes getProfile(String userLoginId) {
        MentorProfile mentorProfile = mentorProfileRepository.findByUser(userRepository.findUserByLoginId(userLoginId).get());

        List<String> certificatesImg = new ArrayList<>();
        List<CertificateImg> certificateImgs = certificateImgRepository.findAllByMentorProfile(mentorProfile).get();
        if (certificateImgs.size() > 0) {
            certificateImgs.forEach(certificateImg -> {
                certificatesImg.add(certificateImg.getImgUrl());
            });
        }

        return ProfileRes.builder()
                .userName(mentorProfile.getUser().getName())
                .location(mentorProfile.getUser().getLocation())
                .info(mentorProfile.getInfo())
                .nickName(mentorProfile.getNickName())
                .school(mentorProfile.getSchool())
                .schoolImg(mentorProfile.getSchoolImg())
                .subject(mentorProfile.getSubject())
                .certificatesImg(certificatesImg)
                .experience(mentorProfile.getExperience())
                .curriculum(mentorProfile.getCurriculum())
                .appeal(mentorProfile.getAppeal())
                .build();
    }

    public List<ProfileRes> getAllProfile() {
        List<ProfileRes> result = new ArrayList<>();
        mentorProfileRepository.findAll().forEach(
                mentorProfile -> {
                    List<String> certificatesImg = new ArrayList<>();
                    List<CertificateImg> certificateImgs = certificateImgRepository.findAllByMentorProfile(mentorProfile).get();
                    if (certificateImgs.size() > 0) {
                        certificateImgs.forEach(certificateImg -> {
                            certificatesImg.add(certificateImg.getImgUrl());
                        });
                    }
                    result.add(
                        ProfileRes.builder()
                                .userName(mentorProfile.getUser().getName())
                                .location(mentorProfile.getUser().getLocation())
                                .info(mentorProfile.getInfo())
                                .nickName(mentorProfile.getNickName())
                                .school(mentorProfile.getSchool())
                                .schoolImg(mentorProfile.getSchoolImg())
                                .subject(mentorProfile.getSubject())
                                .certificatesImg(certificatesImg)
                                .experience(mentorProfile.getExperience())
                                .curriculum(mentorProfile.getCurriculum())
                                .appeal(mentorProfile.getAppeal())
                                .build()
                    );
                }
        );

        return result;
    }
}
