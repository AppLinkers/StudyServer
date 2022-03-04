package com.example.studyBridge_server.service;

import com.example.studyBridge_server.domaion.Certificate;
import com.example.studyBridge_server.domaion.MentorProfile;
import com.example.studyBridge_server.domaion.User;
import com.example.studyBridge_server.dto.userMentor.ProfileRes;
import com.example.studyBridge_server.dto.userMentor.ProfileReq;
import com.example.studyBridge_server.dto.userMentor.ProfileTextReq;
import com.example.studyBridge_server.repository.CertificateRepository;
import com.example.studyBridge_server.repository.MentorProfileRepository;
import com.example.studyBridge_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserMentorService {

    private final MentorProfileRepository mentorProfileRepository;
    private final UserRepository userRepository;
    private final CertificateRepository certificateRepository;
    private final S3Uploader s3Uploader;

    public ProfileRes profile(ProfileReq profileReq) throws IOException {

        // img 업로드
        String schoolImgUrl = s3Uploader.upload(profileReq.getSchoolImg(), "mentor/profile");

        List<List<String>> certificates = new ArrayList<>();

        if (profileReq.getCertificatesImg() != null) {
            for (int i = 0; i < profileReq.getCertificatesImg().get().size();) {
                String certificateImg = s3Uploader.upload(profileReq.getCertificatesImg().get().get(i), "mentor/profile");
                certificates.add(List.of(new String[]{profileReq.getProfileTextReq().getCertificates().get().get(i), certificateImg}));
                i += 1;
            }
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
        mentorProfile.setSubject(profileTextReq.getSubject());
        mentorProfile.setExperience(profileTextReq.getExperience());
        mentorProfile.setCurriculum(profileTextReq.getCurriculum());
        mentorProfile.setAppeal(profileTextReq.getAppeal());

        MentorProfile result = mentorProfileRepository.save(mentorProfile);

        // 기존 자격증 삭제
        Optional<List<Certificate>> searchedCertificates = certificateRepository.findAllByMentorProfile(mentorProfile);

        if (searchedCertificates.isPresent()) {
            certificateRepository.deleteAll(searchedCertificates.get());
        }

        List<Certificate> savedCertificate = new ArrayList<>();
        if (!certificates.isEmpty()) {
            List<Certificate> saveCertificates = new ArrayList<>();
            certificates.forEach(c -> {
                Certificate certificate = new Certificate();

                certificate.setMentorProfile(result);
                certificate.setCertificate(c.get(0));
                certificate.setImgUrl(c.get(1));

                saveCertificates.add(certificate);
            });

            savedCertificate = certificateRepository.saveAll(saveCertificates);
        }

        // 결과 생성
        return ProfileRes.builder()
                .userName(user.getName())
                .location(profileTextReq.getLocation())
                .info(result.getInfo())
                .nickName(result.getNickName())
                .school(result.getSchool())
                .schoolImg(result.getSchoolImg())
                .subject(result.getSubject())
                .certificates(savedCertificate)
                .experience(result.getExperience())
                .curriculum(result.getCurriculum())
                .appeal(result.getAppeal())
                .build();

    }

    public ProfileRes getProfile(String userLoginId) {
        MentorProfile mentorProfile = mentorProfileRepository.findByUser(userRepository.findUserByLoginId(userLoginId).get());

        List<Certificate> certificates = new ArrayList<>();
        Optional<List<Certificate>> searchedCertificates = certificateRepository.findAllByMentorProfile(mentorProfile);
        if (searchedCertificates.isPresent()) {
            certificates = searchedCertificates.get();
        }

        return ProfileRes.builder()
                .userName(mentorProfile.getUser().getName())
                .location(mentorProfile.getUser().getLocation())
                .info(mentorProfile.getInfo())
                .nickName(mentorProfile.getNickName())
                .school(mentorProfile.getSchool())
                .schoolImg(mentorProfile.getSchoolImg())
                .subject(mentorProfile.getSubject())
                .certificates(certificates)
                .experience(mentorProfile.getExperience())
                .curriculum(mentorProfile.getCurriculum())
                .appeal(mentorProfile.getAppeal())
                .build();
    }

    public List<ProfileRes> getAllProfile() {
        List<ProfileRes> result = new ArrayList<>();
        mentorProfileRepository.findAll().forEach(
                mentorProfile -> {
                    List<Certificate> certificates = new ArrayList<>();
                    Optional<List<Certificate>> searchedCertificates = certificateRepository.findAllByMentorProfile(mentorProfile);
                    if (searchedCertificates.isPresent()) {
                        certificates = searchedCertificates.get();
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
                                .certificates(certificates)
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
