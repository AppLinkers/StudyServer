package com.example.studyBridge_server.repository;

import com.example.studyBridge_server.domaion.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {
}