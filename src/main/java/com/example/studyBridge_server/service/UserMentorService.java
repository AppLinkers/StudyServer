package com.example.studyBridge_server.service;

import com.example.studyBridge_server.domain.Certificate;
import com.example.studyBridge_server.domain.MentorProfile;
import com.example.studyBridge_server.domain.User;
import com.example.studyBridge_server.domain.type.Role;
import com.example.studyBridge_server.dto.userMentee.LikeMentorRes;
import com.example.studyBridge_server.dto.userMentor.ProfileReq;
import com.example.studyBridge_server.dto.userMentor.ProfileRes;
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

    /**
     * mentor Profile 등록
     */
    public ProfileRes profile(ProfileReq profileReq) throws IOException {

        String schoolImgUrl;

        if (profileReq.getSchoolImg().getContentType().equals("multipart/form-data")) {
            schoolImgUrl = s3Uploader.upload(profileReq.getSchoolImg(), "mentor/profile");
        } else {
            schoolImgUrl = profileReq.getSchoolImg().getOriginalFilename();
        }



        List<List<String>> certificates = new ArrayList<>();

        if (profileReq.getCertificatesImg() != null) {
            for (int i = 0; i < profileReq.getCertificatesImg().get().size(); i++) {
                String certificateImg;

                if (profileReq.getCertificatesImg().get().get(i).getContentType().equals("multipart/form-data")) {
                    certificateImg = s3Uploader.upload(profileReq.getCertificatesImg().get().get(i), "mentor/profile");
                } else {
                    certificateImg = profileReq.getCertificatesImg().get().get(i).getOriginalFilename();
                }
                certificates.add(List.of(new String[]{profileReq.getCertificates().get().get(i), certificateImg}));
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
        MentorProfile mentorProfile = mentorProfileRepository.findByUser(user).orElseThrow(() -> new RuntimeException("not mentor login id"));
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
        if (certificates.size() > 0) {
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
                .userLoginId(user.getLoginId())
                .userName(user.getName())
                .profileImg(user.getProfileImg())
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

    /**
     * mentorLoginId 의 mentorProfile 조회
     * liked = userLoginId 가 해당 mentor를 '좋아요' 했는지
     */
    public ProfileRes getProfile(String mentorLoginId, String userLoginId) {
        User user = userRepository.findUserByLoginId(userLoginId).get();
        User mentor = userRepository.findUserByLoginId(mentorLoginId).get();

        MentorProfile mentorProfile = mentorProfileRepository.findByUser(mentor).orElseThrow(() -> new RuntimeException("no mentor login id"));

        List<Certificate> certificates = new ArrayList<>();
        Optional<List<Certificate>> searchedCertificates = certificateRepository.findAllByMentorProfile(mentorProfile);
        if (searchedCertificates.isPresent()) {
            certificates = searchedCertificates.get();
        }

        boolean liked = false;

        if (user.getRole().equals(Role.MENTEE)) {
            if (userMenteeService.isLiked(user.getId(), mentor.getId())) {
                liked = true;
            }
        }

        return mentorProfileToProfileRes(mentorProfile, certificates, liked);
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
                    mentorProfileToProfileRes(mentorProfile, certificates, liked)
            );
        }

        return result;
    }

    public ProfileRes mentorProfileToProfileRes(MentorProfile mentorProfile, List<Certificate> certificates, Boolean liked) {
        return ProfileRes.builder()
                .userId(mentorProfile.getUser().getId())
                .userLoginId(mentorProfile.getUser().getLoginId())
                .userName(mentorProfile.getUser().getName())
                .profileImg(mentorProfile.getUser().getProfileImg())
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
}