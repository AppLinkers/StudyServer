package com.example.studyBridge_server.repository;

import com.example.studyBridge_server.domaion.Certificate;
import com.example.studyBridge_server.domaion.MentorProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    Optional<List<Certificate>> findAllByMentorProfile(MentorProfile mentorProfile);

}