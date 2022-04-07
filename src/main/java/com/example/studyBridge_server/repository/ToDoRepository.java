package com.example.studyBridge_server.repository;

import com.example.studyBridge_server.domain.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {

    Optional<List<ToDo>> findAllByStudyId(Long studyId);

    @Modifying
    int deleteAllById(Long id);
}