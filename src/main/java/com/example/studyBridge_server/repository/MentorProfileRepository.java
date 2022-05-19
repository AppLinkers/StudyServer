package com.example.studyBridge_server.repository;

import com.example.studyBridge_server.domain.MentorProfile;
import com.example.studyBridge_server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MentorProfileRepository extends JpaRepository<MentorProfile, Long> {

    Optional<MentorProfile> findByUser(User user);

}