package com.example.studyBridge_server.repository;

import com.example.studyBridge_server.domaion.AssignedToDo;
import com.example.studyBridge_server.domaion.type.ToDoStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssignedToDoRepository extends JpaRepository<AssignedToDo, Long> {

    int countDistinctByUserId(Long userId);

    int countDistinctByUserIdAndStatus(Long userId, ToDoStatus status);

    Optional<List<AssignedToDo>> findAllByUserIdAndStatus(Long userId, ToDoStatus status);

    Optional<List<AssignedToDo>> findAllByToDoId(Long toDoId);

    Optional<List<AssignedToDo>> findAllByUserId(Long userId);
}