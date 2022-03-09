package com.example.studyBridge_server.repository;

import com.example.studyBridge_server.domaion.User;
import com.example.studyBridge_server.domaion.UserAndRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAndRoomRepository extends JpaRepository<UserAndRoom, User> {

    int deleteUserAndRoomByUserId(Long user_id);

}