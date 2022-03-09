package com.example.studyBridge_server.service;

import com.example.studyBridge_server.domaion.Message;
import com.example.studyBridge_server.domaion.UserAndRoom;
import com.example.studyBridge_server.domaion.type.MessageType;
import com.example.studyBridge_server.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final SimpMessageSendingOperations messagingTemplate;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;
    private final UserAndRoomRepository userAndRoomRepository;

    public void send(Message message) {
        String senderName = message.getSenderName();

        if (message.getMessageType().equals(MessageType.ENTER)) {

            // 입장 메시지 전달
            message.setMessage("[알림] " + senderName + " 님이 입장하였습니다.");
            messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoom().getId(), message);
            messageRepository.save(message);

        } else if (message.getMessageType().equals(MessageType.TALK)) {

            // 채팅 처리
            messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoom().getId(), message);
            messageRepository.save(message);

        } else {

            // 채팅방 퇴장 메시지 전달
            message.setMessage("[알림]" + senderName + " 님이 퇴장하였습니다.");
            messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoom().getId(), message);
            messageRepository.save(message);

            // 채팅방 퇴장 처리
            userAndRoomRepository.deleteUserAndRoomByUserId(message.getSenderId());

        }

    }

    public List<Message> findByRoomId(Long room_id) {
        return messageRepository.findByRoom(roomRepository.findById(room_id).get());
    }

}