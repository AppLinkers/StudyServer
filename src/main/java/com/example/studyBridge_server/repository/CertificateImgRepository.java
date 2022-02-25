package com.example.studyBridge_server.repository;

import com.example.studyBridge_server.domaion.CertificateImg;
import com.example.studyBridge_server.domaion.MentorProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CertificateImgRepository extends JpaRepository<CertificateImg, Long> {

    Optional<List<CertificateImg>> findAllByMentorProfile(MentorProfile mentorProfile);

}