package com.example.studyBridge_server.repository;

import com.example.studyBridge_server.domaion.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    Room findRoomByStudyId(Long studyId);

    @Modifying
    int deleteRoomByStudyId(Long studyId);

}