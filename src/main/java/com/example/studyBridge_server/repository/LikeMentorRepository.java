package com.example.studyBridge_server.repository;

import com.example.studyBridge_server.domain.LikeMentor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface LikeMentorRepository extends JpaRepository<LikeMentor, Long> {

    Optional<LikeMentor> findLikeMentorByMenteeIdAndMentorId(Long menteeId, Long mentorId);

    @Modifying
    int deleteLikeMentorByMenteeIdAndMentorId(Long menteeId, Long mentorId);

    @Modifying
    int deleteAllByMenteeId(Long menteeId);

    @Modifying
    int deleteAllByMentorId(Long mentorId);

    Optional<List<LikeMentor>> findLikeMentorsByMenteeId(Long menteeId);
}