package com.example.studyBridge_server.repository;

import com.example.studyBridge_server.domaion.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudyRepository extends JpaRepository<Study, Long> {

    @Query("select u.loginId from User u where u.id = (select s.makerId from Study s where s.id = :studyId)")
    String findMakerLoginIdByStudyId(@Param("studyId") Long studyId);
}