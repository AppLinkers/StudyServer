package com.example.studyBridge_server.repository;

import com.example.studyBridge_server.domaion.User;
import com.example.studyBridge_server.domaion.type.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByLoginId(String loginId);

    @Query("select u.role from User u where u.loginId = :loginId")
    Role findRoleByLoginId(@Param("loginId")String loginId);

    @Query("select u.loginId from User u where u.id = :id")
    String findLoginIdById(@Param("id") Long id);

}