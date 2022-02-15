package com.example.studyBridge_server.repository;

import com.example.studyBridge_server.domaion.Study;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRepository extends JpaRepository<Study, Long> {
}