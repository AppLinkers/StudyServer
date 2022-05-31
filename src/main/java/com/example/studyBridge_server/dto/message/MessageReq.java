package com.example.studyBridge_server.dto.message;

import com.example.studyBridge_server.domain.type.MessageType;
import lombok.Builder;
import lombok.Data;

@Data
public class MessageReq {

    private MessageType messageType; // 생성 및 입장 / 퇴장 / 일반 채팅 / 이미지 채팅 구분

    private Long roomId; // 전송 채팅방

    private Long userId; // 전송자 id

    private String message; // 채팅 본문

    @Builder
    public MessageReq(MessageType messageType, Long roomId, Long userId, String message) {
        this.messageType = messageType;
        this.roomId = roomId;
        this.userId = userId;
        this.message = message;
    }
}
