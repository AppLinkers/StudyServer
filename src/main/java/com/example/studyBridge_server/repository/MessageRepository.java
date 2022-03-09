package com.example.studyBridge_server.repository;

import com.example.studyBridge_server.domaion.Message;
import com.example.studyBridge_server.domaion.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByRoom(Room room);
}