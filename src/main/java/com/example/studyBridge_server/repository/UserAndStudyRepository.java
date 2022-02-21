package com.example.studyBridge_server.repository;

import com.example.studyBridge_server.domaion.Study;
import com.example.studyBridge_server.domaion.User;
import com.example.studyBridge_server.domaion.UserAndStudy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAndStudyRepository extends JpaRepository<UserAndStudy, Long> {

    Optional<UserAndStudy> findByUserAndStudy(User user, Study study);

}