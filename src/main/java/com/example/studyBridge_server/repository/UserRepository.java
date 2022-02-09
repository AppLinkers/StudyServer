package com.example.studyBridge_server.repository;

import com.example.studyBridge_server.domaion.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByLoginId(String loginId);
}