package com.example.studyBridge_server.repository;

import com.example.studyBridge_server.domaion.Study;
import com.example.studyBridge_server.domaion.User;
import com.example.studyBridge_server.domaion.UserAndStudy;
import com.example.studyBridge_server.domaion.type.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserAndStudyRepository extends JpaRepository<UserAndStudy, Long> {

    @Query(value = "SELECT uas FROM UserAndStudy uas WHERE uas.user.loginId = :userLoginId and uas.study.id = :studyId")
    Optional<UserAndStudy> findByUserAndStudy(@Param("userLoginId")String userLoginId, @Param("studyId")Long studyId);

    @Query(value = "SELECT user.login_id FROM user WHERE user.id in (SELECT user_and_study.user_id FROM user_and_study WHERE user_and_study.study_id = :studyId and user_and_study.role = :role)", nativeQuery = true)
    Optional<List<String>> findUserLoginIdByStudyId(@Param("studyId") Long studyId, @Param("role") String role);

    @Modifying
    @Query("delete from UserAndStudy uas where uas.study.id = :studyId and uas.user.id <> :mentorId")
    int chooseMentor(@Param("studyId") Long studyId, @Param("mentorId") Long mentorId);

    int countAllByStudyIdAndRole(Long studyId, Role role);
}