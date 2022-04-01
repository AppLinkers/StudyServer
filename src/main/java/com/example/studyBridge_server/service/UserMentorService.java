package com.example.studyBridge_server.service;

import com.example.studyBridge_server.domaion.Certificate;
import com.example.studyBridge_server.domaion.MentorProfile;
import com.example.studyBridge_server.domaion.User;
import com.example.studyBridge_server.domaion.type.Role;
import com.example.studyBridge_server.dto.userMentee.LikeMentorRes;
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
    private final UserMenteeService userMenteeService;
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
                .userId(user.getId())
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
                .liked(false)
                .build();

    }

    public ProfileRes getProfile(String mentorLoginId, String userLoginId) {
        User user = userRepository.findUserByLoginId(userLoginId).get();
        User mentor = userRepository.findUserByLoginId(mentorLoginId).get();

        MentorProfile mentorProfile = mentorProfileRepository.findByUser(mentor);

        List<Certificate> certificates = new ArrayList<>();
        Optional<List<Certificate>> searchedCertificates = certificateRepository.findAllByMentorProfile(mentorProfile);
        if (searchedCertificates.isPresent()) {
            certificates = searchedCertificates.get();
        }

        Boolean liked = false;

        if (user.getRole().equals(Role.MENTEE)) {
            if (userMenteeService.isLiked(user.getId(), mentor.getId())) {
                liked = true;
            }
        }

        return ProfileRes.builder()
                .userId(mentorProfile.getUser().getId())
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
                .liked(liked)
                .build();
    }

    /**
     * 사용자가 멘토 멘티인지 판단 후, 멘티이면 좋아요 눌렀던 멘토인지 확인
     * 멘토이면 좋아요 값 False 로 설정 후 넘겨주기
     * @param userLoginId
     * @return
     */
    public List<ProfileRes> getAllProfile(String userLoginId) {
        List<ProfileRes> result = new ArrayList<>();
        List<LikeMentorRes> mentorLikeList = new ArrayList<>();

        User user = userRepository.findUserByLoginId(userLoginId).get();

        if (user.getRole().equals(Role.MENTEE)) {
            mentorLikeList = userMenteeService.findLikedMentors(user.getId());
        }

        List<MentorProfile> mentorProfileList = mentorProfileRepository.findAll();

        for (MentorProfile mentorProfile : mentorProfileList) {
            List<Certificate> certificates = new ArrayList<>();
            Optional<List<Certificate>> searchedCertificates = certificateRepository.findAllByMentorProfile(mentorProfile);

            if (searchedCertificates.isPresent()) {
                certificates = searchedCertificates.get();
            }

            Boolean liked = false;

            if (mentorLikeList.contains(new LikeMentorRes(user.getId(), mentorProfile.getUser().getId()))){
                liked = true;
            }

            result.add(
                    ProfileRes.builder()
                            .userId(mentorProfile.getUser().getId())
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
                            .liked(liked)
                            .build()
            );
        }

        return result;
    }
}
