package com.example.studyBridge_server.repository;

import com.example.studyBridge_server.domain.Message;
import com.example.studyBridge_server.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    Optional<List<Message>> findByRoom(Room room);

    @Modifying
    int deleteAllByRoomIdAndUserId(Long roomId, Long userId);
}