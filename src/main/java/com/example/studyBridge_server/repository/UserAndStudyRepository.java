package com.example.studyBridge_server.repository;

import com.example.studyBridge_server.domaion.Study;
import com.example.studyBridge_server.domaion.User;
import com.example.studyBridge_server.domaion.UserAndStudy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserAndStudyRepository extends JpaRepository<UserAndStudy, Long> {

    Optional<UserAndStudy> findByUserAndStudy(User user, Study study);

    @Query(value = "SELECT user.login_id FROM user WHERE user.id in (SELECT user_and_study.user_id FROM user_and_study WHERE user_and_study.study_id = :studyId)", nativeQuery = true)
    List<String> findUserLoginIdByStudyId(@Param("studyId") Long studyId);
}