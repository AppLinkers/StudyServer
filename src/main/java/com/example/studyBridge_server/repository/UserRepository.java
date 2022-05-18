package com.example.studyBridge_server.repository;

import com.example.studyBridge_server.domain.User;
import com.example.studyBridge_server.domain.type.Role;
import com.example.studyBridge_server.dto.userAuth.UserProfileRes;
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

    @Query("select u.name from User u where u.id = :id")
    String findNameById(@Param("id") Long id);

    @Query("select new com.example.studyBridge_server.dto.userAuth.UserProfileRes(u.loginId, u.name, u.phone, u.profileImg, u.location, u.gender, u.role) from User u where u.loginId = :loginId")
    UserProfileRes findProfileByStringId(@Param("loginId") String loginId);
}
