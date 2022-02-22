package com.example.studyBridge_server.repository;

import com.example.studyBridge_server.domaion.MentorProfile;
import com.example.studyBridge_server.domaion.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MentorProfileRepository extends JpaRepository<MentorProfile, Long> {

    MentorProfile findByUser(User user);

}