package com.example.studyBridge_server.domaion;

import com.example.studyBridge_server.domaion.type.MessageType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import javax.persistence.Entity;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Data
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private MessageType messageType; // 생성 및 입장 / 퇴장 / 채팅 구분

    // Many : 1
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Room.class)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "room_id")
    private Room room; // 채팅 방 식별자

    private Long senderId; // 채팅 전송자 식별자
    private String senderName; // 채팅 전송자 아이디
    private String message; // 채팅 본문
}
