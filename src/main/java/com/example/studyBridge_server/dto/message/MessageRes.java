package com.example.studyBridge_server.dto.message;

import com.example.studyBridge_server.domain.type.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageRes {

    private MessageType messageType; // 생성 및 입장 / 퇴장 / 일반 채팅 / 이미지 채팅 구분

    private Long roomId; // 전송 채팅방

    private Long userId;

    private String userName; // 전송자 이름

    private String userProfileImg; // 전송자 프로필 이미지 링크

    private String message; // 채팅 본문
}
