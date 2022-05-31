package com.example.studyBridge_server.domain;

import com.example.studyBridge_server.domain.type.MessageType;
import lombok.Builder;
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

    @Enumerated(value = EnumType.STRING)
    private MessageType messageType; // 생성 및 입장 / 퇴장 / 채팅 구분

    // Many : 1
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Room.class)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "room_id")
    private Room room; // 채팅 방 식별자

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private User user;

    private String message; // 채팅 본문


}
