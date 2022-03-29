package com.example.studyBridge_server.repository;

import com.example.studyBridge_server.domaion.FeedBack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface FeedBackRepository extends JpaRepository<FeedBack, Long> {

    @Modifying
    int deleteAllById(Long id);

    Optional<List<FeedBack>> findAllByAssignedToDoId(Long assignedToDoId);
}