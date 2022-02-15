package com.example.studyBridge_server.repository;

import com.example.studyBridge_server.domaion.UserAndStudy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAndStudyRepository extends JpaRepository<UserAndStudy, Long> {
}