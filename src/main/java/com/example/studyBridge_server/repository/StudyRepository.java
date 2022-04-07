package com.example.studyBridge_server.repository;

import com.example.studyBridge_server.domain.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudyRepository extends JpaRepository<Study, Long> {

    @Query("select u.loginId from User u where u.id = (select s.makerId from Study s where s.id = :studyId)")
    String findMakerLoginIdByStudyId(@Param("studyId") Long studyId);

    @Query("select s.status from Study s where s.id = :studyId")
    String findStatus(@Param("studyId") Long studyId);

    @Modifying
    @Query("update Study s set s.mentorId = null where s.id = :studyId")
    int deleteMentor(@Param("studyId") Long studyId);

    @Query("select s.mentorId from Study s where s.id = :studyId")
    Optional<Long> findMentorIdByStudyId(@Param("studyId") Long studyId);

    @Query("select s.id from Study s where s.mentorId = :mentorId")
    Optional<List<Long>> findAllStudyIdByMentorId(@Param("mentorId") Long mentorId);

    // 내가 멘토인 스터디 목록 반환
    @Query("select s from Study s where s.mentorId = :mentorId")
    Optional<List<Study>> findByMentorId(@Param("mentorId") Long mentorId);

    // 내가 멘티인 스터디 목록 반환
    @Query("select s from Study s where s.id in (select uas.study.id from UserAndStudy uas where uas.user.id = :menteeId)")
    Optional<List<Study>> findByMenteeId(@Param("menteeId") Long menteeId);


}