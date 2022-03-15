package com.example.studyBridge_server.repository;

import com.example.studyBridge_server.domaion.User;
import com.example.studyBridge_server.domaion.UserAndRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface UserAndRoomRepository extends JpaRepository<UserAndRoom, Long> {

    @Modifying
    int deleteUserAndRoomByRoomIdAndUserId(Long roomId, Long userId);
}